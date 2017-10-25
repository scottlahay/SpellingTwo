package scott.spelling.system

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import scott.spelling.model.Grades
import scott.spelling.presenter.SpellingViewModel

class FirebaseRepository(val viewModel: SpellingViewModel) {

    val db = FirebaseDatabase.getInstance()!!
    val key = "grades"

    val gradesListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError?) {
        }

        override fun onDataChange(data: DataSnapshot?) {
            viewModel.grades.value = data!!.getValue(Grades::class.java)!!
        }
    }

    init {
        db.setPersistenceEnabled(true)
        db.getReference(key).setValue(FirebaseStockTheDatabase.grades)  //uncomment this to Update Firebase Database
        db.getReference(key).orderByKey().addValueEventListener(gradesListener)
    }

}

