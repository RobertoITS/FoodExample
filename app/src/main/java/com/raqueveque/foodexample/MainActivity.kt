package com.raqueveque.foodexample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.raqueveque.foodexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter: AdapterExample = AdapterExample()
    private var list: MutableList<ModelExample> = mutableListOf()
    private var originalList: MutableList<ModelExample> = mutableListOf()

    private lateinit var searchMenu: Menu
    private lateinit var itemSearch: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setList()
        originalList.addAll(list)

        setupAdapter()

        setSupportActionBar(binding.toolbar)

        setSearchToolbar()

        var isToolbarShown = false

        binding.plantDetailScrollview.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, _, _ ->
                // User scrolled past image to height of toolbar and the title text is
                // underneath the toolbar, so the toolbar should be shown.
                val shouldShowToolbar = scrollY > binding.toolbar.height
                // The new state of the toolbar differs from the previous state; update
                // appbar and toolbar attributes.
                if (isToolbarShown != shouldShowToolbar) {
                    isToolbarShown = shouldShowToolbar
                    // Use shadow animator to add elevation if toolbar is shown
                    binding.appbar.isActivated = shouldShowToolbar
                }
            }
        )

        binding.rootActivity.setOnClickListener {
            hideKeyboard(this)
        }

    }

    private fun setSearchToolbar() {
        binding.searchToolbar.inflateMenu(R.menu.menu_search)
        searchMenu = binding.searchToolbar.menu
        binding.searchToolbar.setNavigationOnClickListener {
            circleRevealAnimation(R.id.searchToolbar, isShow = false)
        }
        itemSearch = searchMenu.findItem(R.id.action_filter_search)
        MenuItemCompat.setOnActionExpandListener(itemSearch, object : MenuItemCompat.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                //Hacer algo cuando expande
                //Se esconde el titulo
                binding.toolbarLayout.isTitleEnabled = false
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                circleRevealAnimation(R.id.searchToolbar, isShow = false)
                //Se muestra el titulo
                binding.toolbarLayout.isTitleEnabled = true
                return true
            }
        })
        initSearchView()
    }

    @SuppressLint("CutPasteId", "SoonBlockedPrivateApi")
    private fun initSearchView(){
        val searchView = searchMenu.findItem(R.id.action_filter_search).actionView as SearchView
        //Activa/desactiva submit button en el teclado
        searchView.isSubmitButtonEnabled = false
        //Cambia el boton de cerrar
        val closeButton = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn) as ImageView
        closeButton.setImageResource(R.drawable.ic_close)
        //Configuramos colores de texto y pista
        val txtSearch = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = "Buscar.."
        txtSearch.setHintTextColor(Color.GRAY)
        txtSearch.setTextColor(Color.BLACK)
        //Colocamos el cursor
        val searchTextView =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes[searchTextView] =
                R.drawable.search_cursor //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //Metodos de busqueda - SearchView
        /**Este metodo se puede implementar en la clase MainActivity, pero lo vamos
         * a hacer desde el iniciador del searchView*/
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.search(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.search(newText)
                return true
            }
        })
    }

    private fun circleRevealAnimation(id: Int, isShow: Boolean) {
        val view = findViewById<View>(id)
        var width = view.width
        //Las medidas 48 son en dp (density-independent pixels)
        width -= 1 * 48 - 48 / 2
        //Las medidas 36 son en dp (density-independent pixels)
        width -= 36
        val cx = width
        val cy = view.height / 2
        val anim: Animator = if (isShow) ViewAnimationUtils.createCircularReveal(
            view, cx, cy, 0f, width.toFloat())
            else ViewAnimationUtils.createCircularReveal(view, cx, cy, width.toFloat(), 0f)
        anim.duration = 220.toLong()
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            }
        })
        // make the view visible and start the animation
        if (isShow) {
            view.visibility = View.VISIBLE
        }
        // start the animation
        anim.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Maneja la seleccion de items
        return when (item.itemId){
            R.id.action_status -> {
                Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_search -> {
                circleRevealAnimation(R.id.searchToolbar, isShow = true)
                itemSearch.expandActionView()
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setList() {
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("asd", "$400"))
        list.add(ModelExample("Hamburguesa", "$400"))
        list.add(ModelExample("Empanada", "$400"))
        list.add(ModelExample("Helado", "$400"))
        list.add(ModelExample("Panchos", "$400"))
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("Pizza", "$400"))
        list.add(ModelExample("Pizza", "$400"))
    }

    private fun setupAdapter() {
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.isNestedScrollingEnabled = false
        adapter.recyclerAdapter(list, this)
        binding.recycler.adapter = adapter
    }

    //Esconder el teclado
    private fun hideKeyboard(activity: Activity){
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}