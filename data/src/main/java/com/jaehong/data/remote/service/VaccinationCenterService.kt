package com.jaehong.data.remote.service

import com.jaehong.data.remote.model.CenterInfoPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VaccinationCenterService {
    @GET("15077586/v1/centers")
    suspend fun getVaccinationCenters(
        @Query("serviceKey") key: String,
        @Query("page") page: Int,
    ): Response<CenterInfoPage>
}