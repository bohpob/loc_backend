package cz.cvut.fit.poberboh.loc_backend.di

import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDao
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.location.LocationDao
import cz.cvut.fit.poberboh.loc_backend.dao.incidents.location.LocationDaoImpl
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDao
import cz.cvut.fit.poberboh.loc_backend.dao.users.UserDaoImpl

object DaoProvider {
    fun provideUserEntityDao(): UserDao = UserDaoImpl()
    fun provideIncidentDao(): IncidentDao = IncidentDaoImpl()
    fun provideLocationDao(): LocationDao = LocationDaoImpl()
}