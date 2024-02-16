package com.keru.novelly.domain.use_cases

interface StreakUseCase {

    suspend fun incrementStreak()
    suspend fun resetStreak()

}