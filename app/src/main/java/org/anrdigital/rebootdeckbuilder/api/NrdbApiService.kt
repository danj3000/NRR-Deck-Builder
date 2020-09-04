package org.anrdigital.rebootdeckbuilder.api
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NrdbApiService {
    @GET("public/decklists/by_date/{date}")
    fun getDateDeckLists(@Path("date") date: String): Call<NrdbDeckLists>

    @GET("private/decks")
    fun getMyDeckLists(): Call<NrdbDeckLists>
}