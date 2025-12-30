package com.airmon.app.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface StatisticsApiInterface {
    @GET("api/players/statistics/{statistic}/")
    suspend fun getUserRanking(
        @HeaderMap headers: Map<String, String>,
        @Path("statistic") statistic: String
    ): Response<List<RankingUser>>
}

/**
 * @property username
 * @property statistic
 */
data class RankingUser(
    val username: String,
    val statistic: Int
)
