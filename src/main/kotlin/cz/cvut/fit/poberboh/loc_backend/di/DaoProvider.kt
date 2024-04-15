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

object DaoProvider {
    fun provideUserEntityDao(): UserDao = UserDaoImpl()
    fun provideIncidentDao(): IncidentDao = IncidentDaoImpl()
    fun provideLocationDao(): LocationDao = LocationDaoImpl()

    fun provideArchiveIncidentDao(): ArchiveIncidentDao = ArchiveIncidentDaoImpl()
    fun provideArchiveLocationDao(): ArchiveLocationDao = ArchiveLocationDaoImpl()
}