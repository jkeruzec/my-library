package com.example.bibliotheque.data

import android.telecom.ConnectionService
import com.example.bibliotheque.bo.AccountBO
import com.example.bibliotheque.bo.ConnectionInformationBO
import com.example.bibliotheque.data.model.LoggedInUser
import com.example.bibliotheque.services.LibraryService
import com.example.bibliotheque.services.impl.LibraryHauteGoulaineServiceImpl

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var userInformationBO: ConnectionInformationBO? = null
        private set

    val isLoggedIn: Boolean
        get() = userInformationBO != null

    private val hauteGoulaineLibraryService : LibraryService = LibraryHauteGoulaineServiceImpl()

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        userInformationBO = null
    }

    fun logout() {
        userInformationBO = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<ConnectionInformationBO> {
        // handle login
        val result : Result<ConnectionInformationBO> = dataSource.login(username, password)

        if (result is Result.Success) {

            // Fetch user account information
            val accountInformation: AccountBO? = hauteGoulaineLibraryService.getAccountInformation(result.data)

            // What if there is no account information, user is not logged anymore ? show error ?
            if(accountInformation != null) {
                result.data.displayName = accountInformation.accountName
            }

            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(userInformationBO: ConnectionInformationBO) {
        this.userInformationBO = userInformationBO
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
