package com.developers.sleep.dataModels

data class TestQuestion(
    val question: String,
    val answers: List<String>,
)

val QUESTION_LIST_TEST = listOf(
    TestQuestion(
        "What is the capital of France?",
        listOf("London", "Paris", "Berlin", "Madrid"),
    ),
    TestQuestion(
        "Who painted the Mona Lisa?",
        listOf("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo"),
    ),
    TestQuestion(
        "What is the largest planet in our solar system?",
        listOf("Mars", "Venus", "Saturn", "Jupiter"),
    ),
    TestQuestion(
        "Which country won the FIFA World Cup in 2018?",
        listOf("Germany", "Brazil", "France", "Argentina"),
    ),
    TestQuestion(
        "What is the chemical symbol for gold?",
        listOf("Go", "Ag", "Au", "Pt"),
    ),
    TestQuestion(
        "What is the tallest mountain in the world?",
        listOf("Mount Everest", "K2", "Kangchenjunga", "Makalu"),
    )
)
