package cz.cvut.fit.poberboh.loc_backend.di

import cz.cvut.fit.poberboh.loc_backend.dao.archive.incidents.ArchiveIncidentDao
import cz.cvut.fit.poberboh.loc_backend.dao.archive.incidents.ArchiveIncidentDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.archive.locations.ArchiveLocationDao
import cz.cvut.fit.poberboh.loc_backend.dao.archive.locations.ArchiveLocationDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDao
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.locations.LocationDao
import cz.cvut.fit.poberboh.loc_backend.dao.locations.LocationDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDao
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDaoImpl

/**
 * Provides DAO instances.
 */
object DaoProvider {
    /**
     * Provides a user entity DAO.
     *
     * @return The user entity DAO.
     */
    fun provideUserEntityDao(): UserDao = UserDaoImpl()

    /**
     * Provides an incident DAO.
     *
     * @return The incident DAO.
     */
    fun provideIncidentDao(): IncidentDao = IncidentDaoImpl()

    /**
     * Provides a location DAO.
     *
     * @return The location DAO.
     */
    fun provideLocationDao(): LocationDao = LocationDaoImpl()


    /**
     * Provides an archive incident DAO.
     *
     * @return The archive incident DAO.
     */
    fun provideArchiveIncidentDao(): ArchiveIncidentDao = ArchiveIncidentDaoImpl()

    /**
     * Provides an archive location DAO.
     *
     * @return The archive location DAO.
     */
    fun provideArchiveLocationDao(): ArchiveLocationDao = ArchiveLocationDaoImpl()
}