package com.example.minimoneybox.mapper

interface Mapper<A, B> {
    fun map(objectToMap: A): B
}