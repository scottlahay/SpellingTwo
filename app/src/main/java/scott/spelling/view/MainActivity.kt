package scott.spelling.view

import android.R.anim.slide_in_left
import android.R.anim.slide_out_right
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.inputmethodservice.Keyboard
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity.CENTER
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.TextView
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badge
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chibatching.kotpref.Kotpref
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.holder.BadgeStyle
import com.mikepenz.materialdrawer.holder.StringHolder
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.activity_spelling.*
import kotlinx.android.synthetic.main.landing_page.*
import kotlinx.android.synthetic.main.spelling_test_page.*
import scott.spelling.R
import scott.spelling.R.layout.activity_spelling
import scott.spelling.R.style.MyAlertDialogStyle
import scott.spelling.presenter.BasicDialogDetails
import scott.spelling.presenter.MainPage
import scott.spelling.presenter.SpellingViewModel

@GlideModule
class MyAppGlideModule : AppGlideModule()

@Suppress("UNUSED_PARAMETER")
class MainActivity : AppCompatActivity() {
    lateinit var keyboard: Keyboard
    lateinit var viewModel: SpellingViewModel
    lateinit var badgeStyle: BadgeStyle
    lateinit var drawer: Drawer
    var usualTitle = ""
    var mainPages = mutableListOf<Page>()
    var closeApp: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> finish() }

    class Page(var key: MainPage, var view: View)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(activity_spelling)
        setSupportActionBar(myToolbar)
        Kotpref.init(this)
        initUi()
        viewModel = ViewModelProviders.of(this).get(SpellingViewModel::class.java)
        viewModel.landingGradeTitle.observe(this, Observer<String> { it?.let { grade -> setGrade(grade) } })
        viewModel.landingWeekTitle.observe(this, Observer<String> { it?.let { week -> txtWeek.text = "Week $week"; updateBadge(week, 2) } })
        viewModel.appTitle.observe(this, Observer<String> { it?.let { it1 -> setTitle(it1) } })
        viewModel.showPage.observe(this, Observer<MainPage> { it?.let { it1 -> showPage(it1) } })
        viewModel.answerText.observe(this, Observer<String> { it?.let { it1 -> swtAnswer!!.setCurrentText(it1) } })
        viewModel.showCheckMark.observe(this, Observer<Boolean> { it?.let { it1 -> imgCheck!!.visibility = if (it1) VISIBLE else GONE } })
        viewModel.captureTyping.observe(this, Observer<Boolean> { it?.let { it1 -> if (it1) captureTyping() else stopCapturingTyping() } })
        viewModel.completionProgress.observe(this, Observer<Int> { it?.let { it1 -> prgListProgress!!.progress = it1.toFloat() } })
        viewModel.showPopup.observe(this, Observer<BasicDialogDetails> { it?.let { it1 -> showPopup(it1) } })
        viewModel.shiftKeyboard.observe(this, Observer<Boolean> { it?.let { it1 -> shiftKeyboard(it1) } })
//        viewModel.changeDrawerData.observe(this, Observer<List<String>> { it?.let { it1 -> changeDrawerData(it1) } })
        viewModel.selectWeek.observe(this, Observer<List<String>> { it?.let { it1 -> launchWeekChanger(it1) } })
        viewModel.selectGrade.observe(this, Observer<List<String>> { it?.let { it1 -> launchGradeChanger(it1) } })
        mainPages.addAll(listOf(Page(MainPage.ABOUT, layoutAbout), Page(MainPage.LANDING, layoutLanding), Page(MainPage.TEST, layoutSpellingTest)))
        setBackgroundImageOnTheAboutPage()
        changeDrawerData()
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        hideSystemUI()
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    fun setTitle(week: String) {
        usualTitle = week
        title = week
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    fun setBackgroundImageOnTheAboutPage() {
        GlideApp.with(this).load(R.drawable.skipstone).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                layoutAboutBackground.background = resource
            }

        })
    }

    override fun onBackPressed() {
        when {
            drawer.isDrawerOpen                 -> drawer.closeDrawer()
            layoutLanding.visibility == VISIBLE -> super.onBackPressed()
            else                                -> viewModel.goToLandingPage()
        }
    }

    fun showPage(currentPage: MainPage) {
        for (page in mainPages) {
            page.view.visibility = if (page.key == currentPage) View.VISIBLE else View.GONE
        }
    }

    fun setGrade(grade: String) {
        updateBadge(grade, 1)
        txtGrade.text = "Grade $grade"
        prgLoading.visibility = GONE
        txtGrade.visibility = VISIBLE
        txtWeek.visibility = VISIBLE
        btnChangeGrade.visibility = VISIBLE
        btnChangeWeek.visibility = VISIBLE
        btnStart.visibility = VISIBLE
        txtPracticeLabel.text = "Practice Makes Perfect"
    }

    private fun updateBadge(text: String, id: Long) {
        if (::drawer.isInitialized) {
            drawer.updateBadge(id, StringHolder(text))
        }
    }

    fun showPopup(details: BasicDialogDetails) = showPopUp(details.title, details.text, details)
    fun changeWeek(view: View) = viewModel.launchWeekChanger()
    fun changeGrade(view: View) = viewModel.launchGradeChanger()
    fun start(view: View) = viewModel.goToTestPage()

    fun initUi() {
        badgeStyle = BadgeStyle().withColorRes(R.color.primaryColor).withTextColorRes(R.color.primaryTextColor)
        imgCheck!!.icon!!.icon(GoogleMaterial.Icon.gmd_check)
        initTheKeyboard()
        textSwitchStuff()
    }

    fun changeDrawerData() {
        drawer = drawer {
            widthDp = 150
            toolbar = myToolbar
            primaryItem("Home") { onClick { _ -> viewModel.goToLandingPage(); drawer.closeDrawer(); true } }
            primaryItem("Grade") {
                identifier = 1
                onClick { _ -> viewModel.launchGradeChanger(); drawer.closeDrawer(); true }
                badge("0") { colorRes = R.color.primaryColor; textColorRes = R.color.primaryTextColor }
            }
            primaryItem("Week") {
                identifier = 2
                onClick { _ -> viewModel.launchWeekChanger(); drawer.closeDrawer(); true }
                badge("0") { colorRes = R.color.primaryColor; textColorRes = R.color.primaryTextColor }
            }
            primaryItem("About") { onClick { _ -> viewModel.goToAboutPage(); drawer.closeDrawer(); true } }

        }
    }

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

    fun launchWeekChanger(weeks: List<String>) {
        MaterialDialog.Builder(this)
                .title("Change Week")
                .items(weeks)
                .itemsCallback { _, _, _, text -> viewModel.setWeek(text.toString()) }
                .positiveText("Cancel")
                .cancelable(true)
                .onPositive { _, _ -> }
                .show()
    }

    fun launchGradeChanger(grades: List<String>) {
        MaterialDialog.Builder(this)
                .title("Change Grade")
                .items(grades)
                .itemsCallback { _, _, _, text -> viewModel.setGrade(text.toString()) }
                .positiveText("Cancel")
                .cancelable(true)
                .onPositive { _, _ -> }
                .show()
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

    fun textSwitchStuff() {
        swtAnswer!!.setFactory {
            val myText = TextView(this@MainActivity)
            myText.gravity = CENTER
            myText.textSize = 50f
            myText.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
            myText.setTextIsSelectable(false)
            myText
        }
        swtAnswer!!.inAnimation = loadAnimation(this, slide_in_left)
        swtAnswer!!.outAnimation = loadAnimation(this, slide_out_right)
    }

    fun sendEmail(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, "info.skipstone@gmail.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from Spelling App")
        startActivity(intent)
    }

    fun launchWebsite(view: View) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://skipstone.net")))

    inner class MyKeyPressListener : ScottsKeyPressListener() {
        override fun onPress(primaryCode: Int) {
            viewModel.keyPressed(primaryCode.toChar())
        }
    }


}
