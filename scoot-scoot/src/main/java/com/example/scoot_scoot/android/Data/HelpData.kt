package com.example.scoot_scoot.android.Data

import androidx.compose.ui.text.AnnotatedString

object HelpData {
    var questions = arrayListOf(
        HelpDataModel(
            "How can I unlock free rides and other exciting rewards?",
            AnnotatedString("We've got a treat for you! Inside the app, you'll find our thrilling lootbox feature. Purchase lootboxes and open them to reveal amazing rewards, including free rides, VIP status, and even the chance to ride on a magical unicorn scooter. It's like Christmas morning, but with scooters!")
        ),
        HelpDataModel(
            "How do I charge my account balance?",
            AnnotatedString("To charge up your account balance, simply navigate to the \"Balance\" section in the app. There, you'll find an option to add funds. Boost your balance and become the ultimate scooter tycoon. Show off your wealth and ride in style!")
        ),
        HelpDataModel(
            "Any tips for advanced scooter maintenance?", AnnotatedString(
                "Absolutely! Here are a few tips to level up your scooter maintenance game:\n" +
                        "\n" +
                        "\u25CF Explore our \"Scooter Style\" section in the app to customize your scooter with flashy accessories, from neon lights to fur-covered handlebars. Let your scooter reflect your unique personality!\n" +
                        "\n" +
                        "\u25CF Upgrade your scooter's horn to a musical extravaganza. Choose from a range of options like jazz, rock, or even opera. Serenade the streets with style!\n" +
                        "\n" +
                        "\u25CF Keep an eye out for secret treasure chests scattered around the city. Use the app's treasure map feature to find them and unlock exclusive rewards like extra ride credits or the legendary Golden Scooter."
            )
        ),
        HelpDataModel(
            "What should I do if my scooter acts strangely or doesn't start??", AnnotatedString(
                "Don't panic! Try these unconventional solutions:\n" +
                        "\n" +
                        "\u25CFPerform an impromptu dance around the scooter to banish any mischievous spirits.\n" +
                        "\n" +
                        "\u25CFOffer your scooter a quirky nickname and extend a peace treaty. Sometimes a little recognition goes a long way!\n" +
                        "\n" +
                        "\u25CFActivate the app's \"Ghost Mode\" feature and challenge any supernatural beings to a friendly race. Show them who's boss!"
            )
        ),
        HelpDataModel(
            "How can I get assistance or contact support?",
            AnnotatedString("If you need help or have any questions, our support team is here to save the day! \n" +
                    "You can reach out to them through the app's support section. \n" +
                    "They're experts at solving scooter mysteries and providing top-notch assistance!")
        )
    )
}