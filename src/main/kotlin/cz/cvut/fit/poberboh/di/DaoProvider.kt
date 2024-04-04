package cz.cvut.fit.poberboh.di

import cz.cvut.fit.poberboh.data.incidents.IncidentDao
import cz.cvut.fit.poberboh.data.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.data.incidents.location.LocationDao
import cz.cvut.fit.poberboh.data.incidents.location.LocationDaoImpl
import cz.cvut.fit.poberboh.data.users.UserEntityDao
import cz.cvut.fit.poberboh.data.users.UserEntityDaoImpl

object DaoProvider {
    fun provideUserEntityDao(): UserEntityDao = UserEntityDaoImpl()
    fun provideIncidentDao(): IncidentDao = IncidentDaoImpl()
    fun provideLocationDao(): LocationDao = LocationDaoImpl()
}