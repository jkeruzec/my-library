package com.example.bibliotheque.async.task.bo

data class RequestBO(val baseURL : String, val specificEntryPoint : String, val cookies : List<String>) {
}