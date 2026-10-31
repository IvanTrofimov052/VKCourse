# VKCourse

Учебное приложение на Jetpack Compose. Стартовый экран — список приложений.
Нажатие на строку открывает карточку выбранного приложения. Внизу карточки
расположен блок «Витрина»: `HorizontalPager`, ровно три страницы по три приложения
и индикатор текущей страницы. Нажатие на любое приложение витрины возвращает
к списку без дублирования экранов в back stack.

`AppListItem` переиспользуется в списке и витрине. Данные заданы в `LocalAppDataSource`,
навигация — в `AppNavHost` через Navigation Compose (`NavHost`, `composable`,
`rememberNavController`). Интернет используется только для загрузки изображений.

Приложение разделено на слои Clean Architecture внутри модуля `app`:

- `data`: `LocalAppDataSource` хранит хардкод в `AppDetailsDto`,
  `AppDetailsMapper` преобразует DTO в доменную модель, `AppRepositoryImpl`
  реализует интерфейс репозитория.
- `domain`: модели `AppDetails`, `Category` и интерфейс `AppRepository`
  для получения списка, карточки по ID и приложений витрины. Слой не зависит
  от Android, Compose, DTO или реализации источника данных.
- `presentation`: Compose-верстка, навигация, темы, `AppListViewModel` и
  `AppDetailsViewModel`. ViewModel обращаются только к `AppRepository` из domain,
  а Route-компоненты собирают `StateFlow` с учётом lifecycle и передают данные в верстку.

Зависимости направлены к domain: `presentation → domain ← data`.
`di/AppContainer` создаёт источник, Mapper и реализацию Repository;
`MainActivity` передаёт интерфейс репозитория в навигацию. Фабрики создают
ViewModel в соответствующих элементах back stack.

Путь данных: `LocalAppDataSource → AppDetailsDto → AppDetailsMapper → AppDetails → AppRepository → ViewModel → Screen`.
Все методы репозитория возвращают
доменные модели. Mapper переводит строковую категорию DTO в `Category`
(неизвестная категория становится `APP`) и заменяет отсутствующий список
скриншотов пустым. Если карточка по ID не найдена, показывается сообщение
с возможностью вернуться назад. Источник локальный и синхронный.

Список использует MVVM: `AppListViewModel` читает репозиторий один раз при создании и
хранит `AppListUiState` в `StateFlow`. `AppListRoute` получает ViewModel из текущего
элемента back stack навигации и собирает состояние с учётом lifecycle, а
`AppListScreen` отображает данные и передаёт действия пользователя. При повороте
экрана ViewModel сохраняется, каталог не запрашивается повторно; `LazyColumn`
сохраняет позицию прокрутки. `AppDetailsViewModel` аналогично получает карточку
и витрину один раз при создании.

Нажатие на toolbar списка показывает Snackbar. Событие передаётся через
`SharedFlow` с `replay = 0` отдельно от состояния списка. Подписка активна только
на видимом экране (`STARTED`), поэтому уже показанный Snackbar после поворота
или возврата из карточки не появляется снова.

Карточка перенесена из [практического репозитория VkEducation](https://github.com/maltsevmicyou/VkEducation/tree/b4c673ba2c65fdab3277c144aae300947e43dcb6/app/src/main/java/io/mmaltsev/vkeducation/presentation/appdetails)
(коммит `b4c673ba2c65fdab3277c144aae300947e43dcb6`): `AppDetailsContent`,
`AppDetailsHeader`, `Toolbar`, `InstallButton`, `ScreenshotsList`, `AppDescription`,
`Developer`, модели `AppDetails` и `Category`. Адаптированы пакеты и ресурсы,
данные передаются из локального каталога вместо API/Hilt/Room, добавлена витрина.
Для скриншотов используются реальные URL из модели вместо заглушек «1», «2», «3».
Названия и URL приложений витрины взяты из первых девяти элементов `SimilarAppsApi`;
описания и остальные поля заполнены локально. Кнопки установки, отправки и
разработчика показывают сообщение «В процессе разработки», как в практике.

Unit-тесты проверяют преобразование полей DTO, категории и отсутствующих
скриншотов, получение каталога, витрины и карточек через Repository, а также
однократную загрузку данных обеими ViewModel и поведение событий Snackbar.
Тесты ViewModel используют подставную реализацию интерфейса `AppRepository`.

Проверка: `./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest`.
UI-проверки на подключённом устройстве/эмуляторе: `./gradlew :app:connectedDebugAndroidTest`.
Для ручной проверки прокрутите список и поверните экран: содержимое и позиция
должны сохраниться. Нажмите на toolbar и поверните экран во время показа Snackbar:
сообщение должно исчезнуть и появляться только после нового нажатия.
