package info.mrprogrammer.mrpillstracker.core.domain.model

import io.realm.RealmList
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
    var pillDiscription: String = "",
    var pillName: String = "",
    var selectedicon: Int = 0,
    var time: String = "",
    var takenStatus: RealmList<MedicineTakenStatus>? = null,
    var totalMedicineForToday: Int = 0,
    var totalTakeMedicineToday:Int = 0
) : RealmObject()


@RealmClass
open class MedicineTakenStatus(
    @PrimaryKey
    var id:String = "",
    var medicineId:String = "",
    var date: String = "",
    var time: String = "",
    var status: String = "",
): RealmObject()