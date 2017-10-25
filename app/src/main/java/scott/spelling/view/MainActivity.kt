package scott.spelling.view

import android.R.anim.slide_in_left
import android.R.anim.slide_out_right
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.inputmethodservice.Keyboard
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity.CENTER
import android.view.View
import android.view.View.*
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.TextView
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.activity_spelling.*
import scott.spelling.R
import scott.spelling.R.layout.activity_spelling
import scott.spelling.R.style.MyAlertDialogStyle
import scott.spelling.model.Grades
import scott.spelling.presenter.SpellingPresenter
import scott.spelling.presenter.SpellingViewModel

class MainActivity : AppCompatActivity() {

    var presenter: SpellingPresenter? = null
    lateinit var keyboard: Keyboard
    lateinit var progress: ProgressDialog
    lateinit var drawer: Drawer

    var closeApp: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> finish() }
    var grade = 2
    lateinit var badgeStyle: BadgeStyle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        window.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContentView(activity_spelling)
        setSupportActionBar(myToolbar)
        initUi()
        presenter = SpellingPresenter(this)

        val viewModel = ViewModelProviders.of(this).get(SpellingViewModel::class.java)
        viewModel.grades.observe(this, Observer<Grades> { gradesHaveBeenUpdated(it) })

    }

    private fun gradesHaveBeenUpdated(grades: Grades?) {
        presenter!!.updateTheGrades(grades)
        fillTheBurgerMenu(grades)
    }

    internal fun initUi() {

        badgeStyle = BadgeStyle().withColorRes(R.color.primary).withTextColorRes(R.color.primary_text)
        imgCheck!!.setIcon(GoogleMaterial.Icon.gmd_check)
        initTheKeyboard()
        waitForTheProgramToLoad()
        textSwitchStuff()
    }

    fun fillTheBurgerMenu(grades: Grades?) {

        val temp = mutableListOf<IDrawerItem<Any?, RecyclerView.ViewHolder>>()
        grades!!.grades.forEach { drawer(it.name) }

        drawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(myToolbar)
                .withHeader(R.layout.my_material_drawer)
                .withDrawerItems(temp)
                .withSelectedItemByPosition(2)
                .withOnDrawerItemClickListener { _, position, drawerItem ->
                    grade = position
                    for (item in drawer.drawerItems) {
                        (item as PrimaryDrawerItem).withBadge(null as StringHolder?)
                        drawer.updateItem(item)
                    }
                    (drawerItem as PrimaryDrawerItem).withBadge("X").withBadgeStyle(badgeStyle)
                    drawer.updateItem(drawerItem)
                    false
                }
                .build()
    }

    private fun drawer(grade: String) = PrimaryDrawerItem().withName("Grade " + grade)

    private fun drawer2(grade: String) = PrimaryDrawerItem().withBadge("X").withBadgeStyle(badgeStyle).withName("Grade " + grade)

    fun initTheKeyboard() {
        keyboard = Keyboard(this, R.xml.my_keyboard)
        keyboardView!!.keyboard = keyboard
        keyboardView!!.isPreviewEnabled = false
        captureTyping()
    }

    fun captureTyping() = keyboardView!!.setOnKeyboardActionListener(MyKeyPressListener())

    fun stopCapturingTyping() {
        keyboardView!!.setOnKeyboardActionListener(object : ScottsKeyPressListener() {
            override fun onPress(primaryCode: Int) {}/* yup, do nothing*/
        })
    }

    fun shiftKeyboard(shifted: Boolean) {
        keyboard.isShifted = shifted
        keyboardView!!.invalidateAllKeys()
    }

    fun waitForTheProgramToLoad() {
        progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Please wait while loading...")
        progress.setCancelable(false)
    }

    fun showPopUp(title: String, message: String, listener: DialogInterface.OnClickListener) {
        val dialog = AlertDialog.Builder(this, MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("GO", listener).show()
        val view = dialog.findViewById<View>(android.R.id.message) as TextView?

        if (view != null) {
            view.textSize = 40f
            view.gravity = CENTER
        }
    }

    fun popUpYouFinished() {
        AlertDialog.Builder(this, MyAlertDialogStyle)
                .setCancelable(false)
                .setTitle("Finished!")
                .setPositiveButton("Try Again") { dialog, which -> presenter!!.startOver() }.setNegativeButton("Exit") { dialog, which -> finish() }
                .show()
    }

    fun textSwitchStuff() {
        swtAnswer!!.setFactory {
            val myText = TextView(this@MainActivity)
            myText.gravity = CENTER
            myText.textSize = 50f
            myText.setTextColor(Color.WHITE)
            myText.setTextIsSelectable(false)
            myText
        }
        swtAnswer!!.inAnimation = loadAnimation(this, slide_in_left)
        swtAnswer!!.outAnimation = loadAnimation(this, slide_out_right)
    }

    fun setCompletionProgress(currentProgress: Int) {
        prgListProgress!!.progress = currentProgress.toFloat()
    }

    fun setTheAnswer(answerText: String) {
        swtAnswer!!.setCurrentText(answerText)
    }

    fun showProgress() {
        progress.show()
    }

    fun hideProgress() {
        progress.dismiss()
    }

    fun showTest() = showView(GONE, VISIBLE)

    private fun showView(chooseListVisibility: Int, spellingTestVisibility: Int) {
//        setWifiState(chooseListVisibility == VISIBLE)
        layoutTest!!.visibility = spellingTestVisibility
//        layoutSpellingTestTitle!!.visibility = spellingTestVisibility
        keyboardView!!.visibility = spellingTestVisibility
    }

    fun showCheck() {
        imgCheck!!.visibility = VISIBLE
    }

    fun hideCheck() {
        imgCheck!!.visibility = GONE
    }

    internal inner class MyKeyPressListener : ScottsKeyPressListener() {
        override fun onPress(primaryCode: Int) {
            presenter!!.keyPressed(primaryCode.toChar())
        }
    }

    internal inner class ChangeSpellingListListener : OnClickListener {
        override fun onClick(v: View) {
            presenter!!.changeSpellingList()
        }
    }

    fun setWifiState(enabled: Boolean) {
        (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = enabled
    }

    companion object {

        val TAG_IT = "dddddddddddddd"
    }
}
