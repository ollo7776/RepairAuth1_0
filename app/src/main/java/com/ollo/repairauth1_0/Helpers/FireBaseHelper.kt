package com.ollo.repairauth1_0.Helpers

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ollo.repairauth1_0.Model.RecordModel

class FireBaseHelper {
    companion object {
        private lateinit var dbRef: DatabaseReference

        fun insertRepair(
            number: String,
            description: String,
            tryToRepair: String,
            status: String,
            iconList: ArrayList<Int>,
            startDate: String,
            checkDateList: ArrayList<String>,
            endDate: String,
            infoToRemove: Boolean,
            user: String

        ): String {
            var dbMesage = "0"
            dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
            val repairId = dbRef.push().key!!
            val repair = RecordModel(
                repairId,
                number,
                description,
                tryToRepair,
                status,
                iconList,
                startDate,
                checkDateList,
                endDate,
                infoToRemove,
                user
            )
            dbRef.child(repairId).setValue(repair).addOnCompleteListener {
                dbMesage = "1"
            }.addOnFailureListener { err ->
                dbMesage = "Error ${err.message}"
            }
            return dbMesage
        }

        fun deleteRecord(id: String) {
            FirebaseDatabase.getInstance().getReference("Repairs").child(id).removeValue()
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot succesfully deleted!"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error deleting document",
                        e
                    )
                }
        }

        fun infoToRemove(id: String) {
            dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
            dbRef.child(id).child("infoToRemove").setValue(true)
        }

        fun addCheckDate(id: String, checkDateList: java.util.ArrayList<String>?) {
            dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
            dbRef.child(id).child("checkDateList").setValue(checkDateList)
        }
        fun addEndDate(id: String, endDate: String) {
            dbRef = FirebaseDatabase.getInstance().getReference("Repairs")
            dbRef.child(id).child("endDate").setValue(endDate)
        }

    }
}