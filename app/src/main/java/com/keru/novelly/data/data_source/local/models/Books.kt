package com.keru.novelly.data.data_source.local.models

data class Book(
    var bid: String = "",
    var title: String = "",
    var description: String = "",
    var authorId: String = "",
    var authorName: String = "",
    var image: String = "",
    var book: String = "",
    var category: String = "",
    var datePublished: Long = System.currentTimeMillis(),
    var dateUploaded: Long = System.currentTimeMillis(),
    var best: String = "",
    var rating: Float = 0f,
    var downloads: Int = 0,
    val searchTerms: List<String> = listOf()
)

val genres =
    listOf(
        "Adventure",
        "Adult",
        "Autobiography",
        "Art & Photography",
        "Business & Money",
        "Biography",
        "Crafts, Hobbies & Home",
        "Cooking",
        "Dystopian",
        "Education & Teaching",
        "Fiction",
        "Fantasy",
        "Family & Relationships",
        "Health & Fitness",
        "History",
        "Horror",
        "Humor & Entertainment",
        "Inspirational",
        "Kids",
        "LGBTQ+",
        "Law & Criminology",
        "Mystery & Detective",
        "Novel",
        "Personal Development",
        "Philosophy",
        "Poem",
        "Politics & Social Sciences",
        "Religion & Spirituality",
        "Romance",
        "Sci-fi",
        "Thriller",
        "Travel",
        "Textbook",
    )
        .shuffled()

data class GenreDetails(
    val emoji: String,
    val name: String
)

val genreDetailsList = listOf(
    GenreDetails("🌍", "Adventure"),
    GenreDetails("🔞", "Adult"),
    GenreDetails("📖", "Autobiography"),
    GenreDetails("🎨", "Art & Photography"),
    GenreDetails("💰", "Business & Money"),
    GenreDetails("📘", "Biography"),
    GenreDetails("🛠️", "Crafts, Hobbies & Home"),
    GenreDetails("🍳", "Cooking"),
    GenreDetails("🚀", "Dystopian"),
    GenreDetails("📚", "Education & Teaching"),
    GenreDetails("📖", "Fiction"),
    GenreDetails("🧙", "Fantasy"),
    GenreDetails("👨‍👩‍👧‍👦", "Family & Relationships"),
    GenreDetails("💪", "Health & Fitness"),
    GenreDetails("📜", "History"),
    GenreDetails("👻", "Horror"),
    GenreDetails("😂", "Humor & Entertainment"),
    GenreDetails("💡", "Inspirational"),
    GenreDetails("👶", "Kids"),
    GenreDetails("🏳️‍🌈", "LGBTQ+"),
    GenreDetails("⚖️", "Law & Criminology"),
    GenreDetails("🕵️‍♂️", "Mystery & Detective"),
    GenreDetails("📖", "Novel"),
    GenreDetails("📈", "Personal Development"),
    GenreDetails("🤔", "Philosophy"),
    GenreDetails("✒️", "Poem"),
    GenreDetails("🏛️", "Politics & Social Sciences"),
    GenreDetails("🙏", "Religion & Spirituality"),
    GenreDetails("💑", "Romance"),
    GenreDetails("🚀", "Sci-fi"),
    GenreDetails("🔪", "Thriller"),
    GenreDetails("✈️", "Travel"),
    GenreDetails("📚", "Textbook")
)
