package cz.cvut.fit.poberboh.loc_backend.network.auth

/**
 * Represents a refresh tokens.
 */
object RefreshTokens {
    /**
     * The refresh tokens.
     */
    private val refreshTokens = mutableMapOf<String, String>()

    /**
     * Adds a refresh token.
     *
     * @param userId The user ID.
     * @param refreshToken The refresh token.
     */
    fun addRefreshToken(userId: String, refreshToken: String) {
        refreshTokens[refreshToken] = userId
    }

    /**
     * Reads a user by refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The user ID.
     */
    fun readUserByRefreshToken(refreshToken: String): String? {
        return refreshTokens[refreshToken]
    }
}