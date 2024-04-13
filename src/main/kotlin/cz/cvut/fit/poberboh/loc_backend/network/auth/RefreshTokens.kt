package cz.cvut.fit.poberboh.loc_backend.network.auth

object RefreshTokens {
    private val refreshTokens = mutableMapOf<String, String>()

    fun addRefreshToken(userId: String, refreshToken: String) {
        refreshTokens[refreshToken] = userId
    }

    fun readUserByRefreshToken(refreshToken: String): String? {
        return refreshTokens[refreshToken]
    }
}