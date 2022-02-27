package com.example.minimoneybox.mapper.response

import com.example.minimoneybox.mapper.Mapper
import com.example.minimoneybox.model.local.Session
import com.example.minimoneybox.model.response.ResponseLogin
import com.example.minimoneybox.model.response.ResponseSession
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionMapper @Inject constructor() : Mapper<ResponseLogin, Session> {

    override fun map(objectToMap: ResponseLogin): Session {
        return mapSession(objectToMap.Session)
    }

    private fun mapSession(responseSession: ResponseSession?): Session {
        return Session(responseSession?.BearerToken.orEmpty())
    }
}