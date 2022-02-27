package com.example.minimoneybox.mapper.request

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.AuthorizationData
import com.example.minimoneybox.model.request.RequestAuthorizationData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestAuthorizationDataMapper @Inject constructor(): Mapper<AuthorizationData, RequestAuthorizationData> {

    override fun map(objectToMap: AuthorizationData): RequestAuthorizationData {
        return RequestAuthorizationData(
            Email = objectToMap.email,
            Password = objectToMap.password,
            Idfa = "ANYTHING"
        )
    }
}