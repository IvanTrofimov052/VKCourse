# VKCourse

Учебное приложение на Jetpack Compose. Стартовый экран — список приложений.
Нажатие на строку открывает карточку выбранного приложения. Внизу карточки
расположен блок «Витрина»: `HorizontalPager`, ровно три страницы по три приложения
и индикатор текущей страницы. Нажатие на любое приложение витрины возвращает
к списку без дублирования экранов в back stack.

`AppListItem` переиспользуется в списке и витрине. Данные заданы в `AppCatalog`,
навигация — в `AppNavHost` через Navigation Compose (`NavHost`, `composable`,
`rememberNavController`). Интернет используется только для загрузки изображений.

Карточка перенесена из [практического репозитория VkEducation](https://github.com/maltsevmicyou/VkEducation/tree/b4c673ba2c65fdab3277c144aae300947e43dcb6/app/src/main/java/io/mmaltsev/vkeducation/presentation/appdetails)
(коммит `b4c673ba2c65fdab3277c144aae300947e43dcb6`): `AppDetailsContent`,
`AppDetailsHeader`, `Toolbar`, `InstallButton`, `ScreenshotsList`, `AppDescription`,
`Developer`, модели `AppDetails` и `Category`. Адаптированы пакеты и ресурсы,
данные передаются из локального каталога вместо API/Hilt/Room, добавлена витрина.
Для скриншотов используются реальные URL из модели вместо заглушек «1», «2», «3».
Названия и URL приложений витрины взяты из первых девяти элементов `SimilarAppsApi`;
описания и остальные поля заполнены локально. Кнопки установки, отправки и
разработчика показывают сообщение «В процессе разработки», как в практике.

Проверка: `./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug`.
UI-проверки на подключённом устройстве/эмуляторе: `./gradlew :app:connectedDebugAndroidTest`.
