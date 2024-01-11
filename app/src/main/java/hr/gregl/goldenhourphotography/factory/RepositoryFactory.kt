package hr.gregl.goldenhourphotography.factory

import android.content.Context
import hr.gregl.goldenhourphotography.dao.TimeSqlHelper

fun getTimeRepository(context: Context?) = TimeSqlHelper(context)