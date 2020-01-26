package com.example.bibliotheque.data

import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.data.model.LoggedInUser
import com.example.bibliotheque.services.LibraryService
import com.example.bibliotheque.services.impl.LibraryHauteGoulaineServiceImpl
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private val hauteGoulaineLibraryService : LibraryService = LibraryHauteGoulaineServiceImpl()

    fun login(username: String, password: String): Result<ConnectionInformationBO> {
            val userInformation : ConnectionInformationBO = this.hauteGoulaineLibraryService.authenticate(username, password)
        return when(userInformation.isAuthenticated()) {
            true -> Result.Success(userInformation)
            false -> Result.Error(IOException("User is not logged"))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

