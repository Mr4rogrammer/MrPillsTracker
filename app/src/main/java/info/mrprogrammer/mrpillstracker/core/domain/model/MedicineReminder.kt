package info.mrprogrammer.mrpillstracker.core.domain.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class MedicineReminder(
    @PrimaryKey
    var id:String = "",
    var afterOrBefore: String  = "",
    var count: String = "",
    var date: String = "",
    var everyFiveReminder: Boolean = false,
    var noOfDay: String = "",
    var notification: String = "",
    var pillDescription: String = "",
    var pillName: String = "",
    var selectedIcon: Int = 0,
    var time: String = "",
    var usedPills: String = ""
) : RealmObject()
