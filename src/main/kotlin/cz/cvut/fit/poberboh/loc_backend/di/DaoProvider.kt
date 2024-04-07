package cz.cvut.fit.poberboh.loc_backend.di

import cz.cvut.fit.poberboh.loc_backend.data.incidents.IncidentDao
import cz.cvut.fit.poberboh.loc_backend.data.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.loc_backend.data.incidents.location.LocationDao
import cz.cvut.fit.poberboh.loc_backend.data.incidents.location.LocationDaoImpl
import cz.cvut.fit.poberboh.loc_backend.data.users.UserEntityDao
import cz.cvut.fit.poberboh.loc_backend.data.users.UserEntityDaoImpl

object DaoProvider {
    fun provideUserEntityDao(): UserEntityDao = UserEntityDaoImpl()
    fun provideIncidentDao(): IncidentDao = IncidentDaoImpl()
    fun provideLocationDao(): LocationDao = LocationDaoImpl()
}