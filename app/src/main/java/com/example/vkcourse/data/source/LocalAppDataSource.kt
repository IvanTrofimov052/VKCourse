package com.example.vkcourse.data.source

import com.example.vkcourse.data.dto.AppDetailsDto

/**
 * Локальный источник хранит исходные данные только в DTO. Сетевого API нет.
 */
class LocalAppDataSource {
    private val featuredApp = AppDetailsDto(
        id = "fa2e31b8-1234-4cf7-9914-108a170a1b01",
        name = "Гильдия Героев: Экшен ММО РПГ",
        developer = "VK Play",
        category = "game",
        ageRating = 12,
        size = 223.7f,
        screenshotUrlList = listOf(
            "https://static.rustore.ru/imgproxy/-y8kd-4B6MQ-1OKbAbnoAIMZAzvoMMG9dSiHMpFaTBc/preset:web_scr_lnd_335/plain/https://static.rustore.ru/apk/393868735/content/SCREENSHOT/dfd33017-e90d-4990-aa8c-6f159d546788.jpg@webp",
            "https://static.rustore.ru/imgproxy/dZCvNtRKKFpzOmGlTxLszUPmwi661IhXynYZGsJQvLw/preset:web_scr_lnd_335/plain/https://static.rustore.ru/apk/393868735/content/SCREENSHOT/60ec4cbc-dcf6-4e69-aa6f-cc2da7de1af6.jpg@webp",
            "https://static.rustore.ru/imgproxy/g5whSI1uNqaL2TUO7TFfM8M63vXpWXNCm2vlX4Ahvc4/preset:web_scr_lnd_335/plain/https://static.rustore.ru/apk/393868735/content/SCREENSHOT/c2dde8bc-c4ab-482a-80a5-2789149f598d.jpg@webp",
            "https://static.rustore.ru/imgproxy/TjeurtC7BczOVJt74XhjGYuQnG1l4rx6zpDqyMb00GY/preset:web_scr_lnd_335/plain/https://static.rustore.ru/apk/393868735/content/SCREENSHOT/08318f76-7a9c-43aa-b4a7-1aa878d00861.jpg@webp",
        ),
        iconUrl = "https://static.rustore.ru/imgproxy/APsbtHxkVa4MZ0DXjnIkSwFQ_KVIcqHK9o3gHY6pvOQ/preset:web_app_icon_62/plain/https://static.rustore.ru/apk/393868735/content/ICON/3f605e3e-f5b3-434c-af4d-77bc5f38820e.png@webp",
        description = "Легендарный рейд героев в Фэнтези РПГ. Станьте героем гильдии и сразите мастера подземелья!",
    )

    private val showcaseApps: List<AppDetailsDto> = listOf(
        AppDetailsDto(
            id = "quick-note",
            name = "QuickNote",
            developer = "QuickNote Studio",
            category = "productivity",
            ageRating = 0,
            size = 18.4f,
            iconUrl = "https://fastly.picsum.photos/id/237/200/200.jpg?hmac=zHUGikXUDyLCCmvyww1izLK3R3k8oRYBRiTizZEdyfI",
            screenshotUrlList = emptyList(),
            description = "Сохраняйте заметки и идеи. Собирайте списки дел и находите нужные записи в одном месте.",
        ),
        AppDetailsDto(
            id = "82ea6f63-2b2a-43e1-b54c-ec62e6e27229",
            name = "Calmalist",
            developer = "Calmalist",
            category = "health",
            ageRating = 0,
            size = 32.1f,
            iconUrl = "https://fastly.picsum.photos/id/238/200/200.jpg?hmac=O4Jc6lqHVfaKVzLf8bWssNTbWzQoaRUC0TDXod9xDdM",
            screenshotUrlList = emptyList(),
            description = "Планируйте день спокойно. Следите за привычками и уделяйте время отдыху.",
        ),
        AppDetailsDto(
            id = "c6f64035-c432-4aad-a550-a1dac2d3b047",
            name = "FitRiver",
            developer = "FitRiver",
            category = "sports",
            ageRating = 0,
            size = 45.6f,
            iconUrl = "https://fastly.picsum.photos/id/239/200/200.jpg?hmac=8JqlXUpZ9Xy0H6tMK8sCPQAYU9vUn9Qa8Kg-U9h3sCY",
            screenshotUrlList = emptyList(),
            description = "Тренируйтесь в удобном темпе. Записывайте активность и следите за своим прогрессом.",
        ),
        AppDetailsDto(
            id = "d22b7cda-2d36-4ebd-bfa3-be58770fa455",
            name = "TaskLoop",
            developer = "TaskLoop",
            category = "productivity",
            ageRating = 0,
            size = 21.8f,
            iconUrl = "https://fastly.picsum.photos/id/240/200/200.jpg?hmac=PZiJibMUuv5Zpv9MAcqLaCYJOPt-XNuDh3RPBZiGcoY",
            screenshotUrlList = emptyList(),
            description = "Планируйте задачи и проекты. Расставляйте приоритеты и отмечайте выполненные дела.",
        ),
        AppDetailsDto(
            id = "f830e414-42a9-4fe6-af16-555a85f6f6c2",
            name = "Pixtrix",
            developer = "Pixtrix",
            category = "photography",
            ageRating = 12,
            size = 67.3f,
            iconUrl = "https://fastly.picsum.photos/id/241/200/200.jpg?hmac=F-mDYyK1xUiXjlLd5PvE8EQpTwGfFndV7mMaANDtl28",
            screenshotUrlList = emptyList(),
            description = "Редактируйте фотографии. Применяйте фильтры и сохраняйте любимые кадры.",
        ),
        AppDetailsDto(
            id = "ba31ce5d-b214-46e9-bf7c-35e99e70af14",
            name = "TinyChef",
            developer = "TinyChef",
            category = "food",
            ageRating = 0,
            size = 38.2f,
            iconUrl = "https://fastly.picsum.photos/id/242/200/200.jpg?hmac=Z3aa8zbEQkEMFgnVh0Pn96vmCZHhJ17qzCrePYksrcY",
            screenshotUrlList = emptyList(),
            description = "Находите рецепты на каждый день. Сохраняйте любимые блюда и составляйте список покупок.",
        ),
        AppDetailsDto(
            id = "204a939c-39fb-4c9d-bed9-daeeba8a465e",
            name = "GymMate",
            developer = "GymMate",
            category = "sports",
            ageRating = 0,
            size = 52.7f,
            iconUrl = "https://fastly.picsum.photos/id/243/200/200.jpg?hmac=fW5ZwzzyTBy2t2MROp988oq12mZnKwN0coFLhZEE87s",
            screenshotUrlList = emptyList(),
            description = "Составляйте программу тренировок. Записывайте упражнения и отслеживайте результаты.",
        ),
        AppDetailsDto(
            id = "1ec98719-1d86-4cdc-b99d-11c6c23d981e",
            name = "FlowCard",
            developer = "FlowCard",
            category = "education",
            ageRating = 0,
            size = 16.9f,
            iconUrl = "https://fastly.picsum.photos/id/244/200/200.jpg?hmac=Q1gdvE6ZPZUX3nXkxvmzuc12eKVZ9XVEmSH3nCJ2OOo",
            screenshotUrlList = emptyList(),
            description = "Учитесь с помощью карточек. Повторяйте материал и запоминайте новые слова.",
        ),
        AppDetailsDto(
            id = "d8180b87-56de-4737-b5ca-953d8c15ec80",
            name = "DreamSafe",
            developer = "DreamSafe",
            category = "health",
            ageRating = 0,
            size = 28.5f,
            iconUrl = "https://fastly.picsum.photos/id/2/200/200.jpg?hmac=isSWZUpv7D1D156XcADPOCZlfCG9mmvb8OlXFdvLdK0",
            screenshotUrlList = emptyList(),
            description = "Ведите дневник сна. Записывайте наблюдения и создавайте вечерние привычки.",
        ),
    )

    private val apps: List<AppDetailsDto> = listOf(featuredApp) + showcaseApps

    fun getApps(): List<AppDetailsDto> = apps

    fun getApp(id: String): AppDetailsDto? = apps.find { it.id == id }

    fun getShowcaseApps(): List<AppDetailsDto> = showcaseApps
}
