package com.developers.sleep.dataModels

data class TestQuestion(
    val question: String,
    val answers: List<String>,
)

val QUESTION_LIST_TEST = listOf(
    TestQuestion(
        "How many hours do you usually sleep at night?",
        listOf("Less than 5 hours", "5-7 hours", "7-9 hours", "More than 9 hours")
    ),
    TestQuestion(
        "Do you often remember your dreams?",
        listOf("Yes, often", "Sometimes", "Rarely", "Never")
    ),
    TestQuestion(
        "How often do you experience difficulty falling asleep?",
        listOf("Never", "Sometimes", "Often", "Every night")
    ),
    TestQuestion(
        "In which position do you usually sleep?",
        listOf("On the stomach", "On the back", "On the side", "Different positions")
    ),
    TestQuestion(
        "Do you typically have a bedtime routine?",
        listOf("Yes, always", "Sometimes", "Rarely", "Never")
    ),
    TestQuestion(
        "How often do you exercise in a week?",
        listOf("Never", "1-2 times", "3-4 times", "5 times or more")
    )
)
