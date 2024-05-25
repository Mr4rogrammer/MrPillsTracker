package info.mrprogrammer.mrpillstracker.core.domain.model

data class MedicineReminderWrapper (
    var totalMedicineForToday:Int = 0,
    var totalTakeMedicineToday:Int = 0,
    var reminders: List<MedicineReminder>? = null
)