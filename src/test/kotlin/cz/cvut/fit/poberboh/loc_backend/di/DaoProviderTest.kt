package cz.cvut.fit.poberboh.loc_backend.di

import cz.cvut.fit.poberboh.loc_backend.dao.archive.incidents.ArchiveIncidentDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.archive.locations.ArchiveLocationDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.locations.LocationDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDaoImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class DaoProviderTest {
    @Test
    fun testProvideUserEntityDao() {
        val userDao = DaoProvider.provideUserEntityDao()
        assertTrue { userDao is UserDaoImpl }
    }

    @Test
    fun testProvideIncidentDao() {
        val incidentDao = DaoProvider.provideIncidentDao()
        assertTrue { incidentDao is IncidentDaoImpl }
    }

    @Test
    fun testProvideLocationDao() {
        val locationDao = DaoProvider.provideLocationDao()
        assertTrue { locationDao is LocationDaoImpl }
    }

    @Test
    fun testProvideArchiveIncidentDao() {
        val archiveIncidentDao = DaoProvider.provideArchiveIncidentDao()
        assertTrue { archiveIncidentDao is ArchiveIncidentDaoImpl }
    }

    @Test
    fun testProvideArchiveLocationDao() {
        val archiveLocationDao = DaoProvider.provideArchiveLocationDao()
        assertTrue { archiveLocationDao is ArchiveLocationDaoImpl }
    }
}