package com.javdc.tussapp.data.remote.api

import com.javdc.tussapp.data.remote.model.TokenResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/realms/TUSSAM-SSO/protocol/openid-connect/token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): TokenResponseDto

    @FormUrlEncoded
    @POST("/auth/realms/TUSSAM-SSO/protocol/openid-connect/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String,
    ): TokenResponseDto

}