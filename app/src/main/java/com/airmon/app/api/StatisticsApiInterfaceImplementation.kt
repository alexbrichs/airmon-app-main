package com.airmon.app.api

import android.app.Application

import com.airmon.app.helpers.getGetApiHeaders
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.runBlocking

object StatisticsApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(StatisticsApiInterface::class.java)

    fun getRankingUsers(application: Application, statistic: String): List<Pair<String, Int>> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<RankingUser>>
        runBlocking {
            response = apiService.getUserRanking(headers, statistic)
        }
        if (response.isSuccessful) {
            val rankingUsers: List<RankingUser>? = response.body()
            return rankingUsers?.map { Pair(it.username, it.statistic) } as MutableList<Pair<String, Int>>
        }
        return emptyList()
    }
}
