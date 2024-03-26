package cz.cvut.fit.poberboh.di

import cz.cvut.fit.poberboh.data.incidents.IncidentDao
import cz.cvut.fit.poberboh.data.incidents.IncidentDaoImpl
import cz.cvut.fit.poberboh.data.incidents.gps.GPSIncidentDao
import cz.cvut.fit.poberboh.data.incidents.gps.GPSIncidentDaoImpl
import cz.cvut.fit.poberboh.data.users.UserEntityDao
import cz.cvut.fit.poberboh.data.users.UserEntityDaoImpl

object DaoProvider {
    fun provideUserEntityDao(): UserEntityDao = UserEntityDaoImpl()
    fun provideIncidentDao(): IncidentDao = IncidentDaoImpl()
    fun provideGPSIncidentDao(): GPSIncidentDao = GPSIncidentDaoImpl()
}