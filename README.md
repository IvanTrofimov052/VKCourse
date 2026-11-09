# VKCourse

Учебное приложение на Jetpack Compose. Стартовый экран — каталог приложений,
загружаемый с сервера. Нажатие на строку открывает карточку выбранного
приложения, данные для которой тоже приходят из сети. Внизу карточки
расположен блок «Витрина»: `HorizontalPager`, страницы по три приложения
и индикатор текущей страницы. Нажатие на любое приложение витрины возвращает
к списку без дублирования экранов в back stack.

## Сеть

Для запросов используется **Retrofit 2** с конвертером **Gson** и клиентом
**OkHttp**. Базовый адрес задан в `app/build.gradle.kts` через
`buildConfigField` и доступен как `BuildConfig.BASE_URL`:

```text
http://185.103.109.134/
```

`NetworkModule` создаёт `OkHttpClient` (таймауты, BASIC-логирование только
в debug), `Retrofit` и реализацию `CatalogApi`. Так как учебный сервер
работает по HTTP, в `res/xml/network_security_config.xml` cleartext разрешён
только для этого IP; в манифесте подключён `networkSecurityConfig`.

Эндпоинты в `data/network/CatalogApi.kt`:

- `GET /catalog` — каталог приложений (`List<CatalogAppDto>`);
- `GET /{id}` — подробная информация о приложении (`AppDetailsDto`);
- `GET /catalog/{id}` — резервный путь для текущего развёртывания сервера:
  запрос `/{id}` на нём отвечает `404`, поэтому `RemoteAppDataSource`
  автоматически повторяет запрос по этому адресу.

Ошибки сети не скрываются: ViewModel переводит экран в состояние ошибки
с кнопкой «Повторить». Ответ `404` для карточки означает, что приложения нет,
и показывается сообщение «Приложение не найдено».

## Слои

Приложение разделено на слои Clean Architecture внутри модуля `app`:

- `data`:
  - `network/CatalogApi` — Retrofit-интерфейс;
  - `dto/CatalogAppDto`, `dto/AppDetailsDto` — ответы сервера;
  - `mapper/CatalogAppMapper`, `mapper/AppDetailsMapper`, `mapper/CategoryMapper` —
    преобразование DTO в доменные модели. API присылает категории по-русски
    («Игры», «Фото и видео»), `CategoryMapper` переводит их в `Category`;
  - `source/RemoteAppDataSource` — вызовы API и резервный путь карточки;
  - `repository/AppRepositoryImpl` — кэширует каталог, чтобы список, витрина
    и повторные открытия не делали лишних запросов.
- `domain`:
  - `CatalogApp` — краткая карточка для списка и витрины;
  - `AppDetails` — подробная карточка;
  - `Category`;
  - `AppRepository` — `suspend`-методы `getApps`, `getApp(id)`,
    `getShowcaseApps`.
- `presentation`: Compose-вёрстка, навигация, темы, `AppListViewModel`
  и `AppDetailsViewModel`. ViewModel обращаются только к `AppRepository`
  из domain, Route-компоненты собирают `StateFlow` с учётом lifecycle.

Зависимости направлены к domain: `presentation → domain ← data`.
Для внедрения зависимостей используется Hilt: `VKCourseApplication` помечен
`@HiltAndroidApp`, `MainActivity` — `@AndroidEntryPoint`. `di/AppModule`
связывает интерфейс `AppRepository` с `AppRepositoryImpl`,
`di/NetworkModule` предоставляет Retrofit и OkHttp. ViewModel помечены
`@HiltViewModel` и запрашивают интерфейс репозитория в конструкторе,
а Route-компоненты создают их через `hiltViewModel()`.
`AppDetailsViewModel` получает `appId` из `SavedStateHandle`, который
заполняется аргументами Navigation Compose.

Путь данных:

```text
CatalogApi → DTO → Mapper → domain → Repository → ViewModel → Screen
```

## Экран каталога

`AppListViewModel` запускает загрузку в `viewModelScope` при создании.
`AppListUiState` содержит список, флаг загрузки и флаг ошибки. Пока данных
нет, показывается `CircularProgressIndicator`; при ошибке — сообщение
и кнопка повтора; после успеха — `LazyColumn` с `CatalogApp`.
Строка `AppListItem` показывает иконку, название, категорию и краткое
описание. ViewModel переживает поворот экрана, поэтому каталог не
запрашивается повторно, а `LazyColumn` сохраняет позицию прокрутки.

Нажатие на toolbar списка показывает Snackbar. Событие передаётся через
`SharedFlow` с `replay = 0` отдельно от состояния списка. Подписка активна
только на видимом экране (`STARTED`), поэтому уже показанный Snackbar после
поворота или возврата из карточки не появляется снова.

## Экран карточки

`AppDetailsViewModel` последовательно загружает карточку и приложения
витрины. `AppDetailsUiState` хранит `AppDetails?`, список витрины, флаги
загрузки и ошибки. При отсутствии приложения показывается сообщение
с возможностью вернуться назад, при ошибке сети — кнопка повтора.
Подробные данные (разработчик, возрастной рейтинг, размер, скриншоты)
приходят из `GET /{id}`, поэтому карточка работает для любого приложения
из серверного каталога, а не только для локального набора.

Витрина собирается из кэшированного каталога: репозиторий берёт девять
элементов после первого (первый считается избранным). `AppListItem`
переиспользуется в списке и витрине.

Карточка перенесена из [практического репозитория VkEducation](https://github.com/maltsevmicyou/VkEducation/tree/b4c673ba2c65fdab3277c144aae300947e43dcb6/app/src/main/java/io/mmaltsev/vkeducation/presentation/appdetails)
(коммит `b4c673ba2c65fdab3277c144aae300947e43dcb6`): `AppDetailsContent`,
`AppDetailsHeader`, `Toolbar`, `InstallButton`, `ScreenshotsList`, `AppDescription`,
`Developer`, модели `AppDetails` и `Category`. Адаптированы пакеты и ресурсы,
данные передаются из API, добавлена витрина. Кнопки установки, отправки
и разработчика показывают сообщение «В процессе разработки», как в практике.

## Тесты

Unit-тесты проверяют:

- преобразование DTO каталога и карточки, включая русские названия категорий
  и отсутствующие скриншоты;
- запрос каталога, кэширование, формирование витрины;
- запрос карточки по `/{id}` и резервный переход на `/catalog/{id}` при `404`;
- состояния загрузки/ошибки/повтора в обеих ViewModel и события Snackbar.

Тесты ViewModel используют `MainDispatcherRule` и подставную реализацию
`AppRepository`, поэтому сеть в unit-тестах не нужна.

Инструментальные тесты запускаются через `HiltTestRunner` и подменяют
`AppRepository` детерминированной реализацией (`AndroidTestData`), поэтому
не зависят от доступности сервера. Они проверяют прокрутку и пересоздание
Activity, Snackbar, навигацию в карточку и страницы витрины.

Проверка:

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
```

UI-проверки на подключённом устройстве/эмуляторе:

```bash
./gradlew :app:connectedDebugAndroidTest
```

Для ручной проверки запустите приложение с интернетом: каталог должен
загрузиться с `http://185.103.109.134/catalog`. При отключённой сети
появляется сообщение с кнопкой «Повторить».
