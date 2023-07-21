package com.congvtt1.smartmovie.movie.fragment.detail

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.congvtt1.domain.model.MovieDetail
import com.congvtt1.smartmovie.R
import com.congvtt1.smartmovie.base.viewmodel.ViewModelFactory
import com.congvtt1.smartmovie.databinding.FragmentDetailBinding
import com.congvtt1.smartmovie.movie.adapter.CastAdapter
import com.congvtt1.smartmovie.movie.adapter.MovieListAdapter
import com.congvtt1.smartmovie.movie.viewmodel.MovieDetailViewModel
import java.util.*

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModels { ViewModelFactory() }
    private lateinit var castAdapter: CastAdapter
    private lateinit var similarMovieAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setOnClick()
        observeData(viewLifecycleOwner)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMovie(args.movieId)
    }

    private fun initView() {
        binding.rcCast.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter()
        binding.rcCast.adapter = castAdapter
        binding.rcSimilarMovies.layoutManager = LinearLayoutManager(requireContext())
        similarMovieAdapter = MovieListAdapter { }
        binding.rcSimilarMovies.adapter = similarMovieAdapter

        binding.rcSimilarMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    setViewAllToTextViewOverview(binding.tvDescription.text.toString())
                }
            }
        })
        binding.scvDetail.setOnScrollChangeListener { _, _, yNew, _, yOld ->
            if (yOld < yNew) {
                setViewAllToTextViewOverview(binding.tvDescription.text.toString())
            }
        }
    }

    private fun setOnClick() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        binding.tvCast.setOnClickListener {
            binding.tvCast.isClickable = false
            binding.tvCast.setTextColor(Color.parseColor("#FF018786"))
            binding.viewCast.visibility = View.VISIBLE
            binding.rcCast.visibility = View.VISIBLE

            binding.tvSimilarMovies.isClickable = true
            binding.tvSimilarMovies.setTextColor(Color.parseColor("#FFC4C4C4"))
            binding.viewSimilarMovies.visibility = View.GONE
            binding.rcSimilarMovies.visibility = View.GONE
        }
        binding.tvSimilarMovies.setOnClickListener {
            binding.tvSimilarMovies.isClickable = false
            binding.tvSimilarMovies.setTextColor(Color.parseColor("#FF018786"))
            binding.viewSimilarMovies.visibility = View.VISIBLE
            binding.rcSimilarMovies.visibility = View.VISIBLE

            binding.tvCast.isClickable = true
            binding.tvCast.setTextColor(Color.parseColor("#FFC4C4C4"))
            binding.viewCast.visibility = View.GONE
            binding.rcCast.visibility = View.GONE
        }
    }

    private fun observeData(lifecycleOwner: LifecycleOwner) {
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.error },
            observer = { error ->
                if (error != null) {
                    Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
        viewModel.store.observe(
            owner = lifecycleOwner,
            selector = { state -> state.movie },
            observer = { data ->
                setupMovieDetailView(data)
            }
        )
        viewModel.store.observeAnyway(
            owner = lifecycleOwner,
            selector = { state -> state.loading },
            observer = { loading ->
                if (loading) {
                    binding.progressLoading.visibility = View.VISIBLE
                } else {
                    binding.progressLoading.visibility = View.GONE
                }
            }
        )
    }

    private fun setupMovieDetailView(data: MovieDetail) {
        binding.tvMovieName.text = data.title
        binding.tvGenres.text = data.category
        binding.rating.rating = (data.rating / 2).toFloat()
        "${String.format(Locale.US, "%.1f", data.rating)} / 10".also {
            binding.tvRate.text = it
        }
        "Language: ${data.language}".also {
            binding.tvLanguage.text = it
        }

        binding.tvTime.text = data.time
        binding.tvDescription.text = data.content
        setViewAllToTextViewOverview(data.content)
        Glide.with(binding.ivImage)
            .load(data.cover)
            .centerCrop()
            .error(R.drawable.default_movie)
            .placeholder(R.drawable.default_movie)
            .into(binding.ivImage)
        castAdapter.submitList(data.actors)
        similarMovieAdapter.submitList(data.similarMovies)
    }

    private fun setViewAllToTextViewOverview(overview: String) {
        if (binding.tvDescription.lineCount > 3) {
            val endOfLine3 = binding.tvDescription.layout.getLineEnd(2)
            val spannableString = SpannableString("view all")
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        binding.tvDescription.text = overview
                    }

                    override fun updateDrawState(textPaint: TextPaint) {
                        textPaint.isUnderlineText = false
                        textPaint.color = ContextCompat.getColor(requireContext(), R.color.teal_700)
                    }
                },
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            "${binding.tvDescription.text.substring(0, endOfLine3 - 12)}... ".also {
                binding.tvDescription.text = it
            }
            binding.tvDescription.append(spannableString)
            binding.tvDescription.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetError()
    }

    companion object {
        fun newInstance() = DetailFragment()
    }
}