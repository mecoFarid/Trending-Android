package com.mecofarid.trending.ui.trending.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.*
import com.mecofarid.shared.R
import com.mecofarid.shared.domain.common.data.DataException
import com.mecofarid.shared.domain.common.data.Operation
import com.mecofarid.shared.domain.common.data.Query
import com.mecofarid.shared.domain.common.data.repository.Repository
import com.mecofarid.shared.domain.common.either.Either
import com.mecofarid.shared.domain.features.trending.domain.interactor.GetTrendingInteractor
import com.mecofarid.shared.domain.features.trending.domain.model.Trending
import com.mecofarid.shared.ui.trending.TrendingViewModel
import com.mecofarid.shared.ui.trending.TrendingViewModel.State
import com.mecofarid.test.anyList
import com.mecofarid.test.feature.repo.anyTrending
import com.mecofarid.trending.common.ui.libs.shimmer.customShimmer
import com.mecofarid.trending.common.ui.preview.SystemUiPreview
import com.mecofarid.trending.common.ui.resources.Dimens
import com.mecofarid.trending.common.ui.resources.TrendingTheme
import io.reactivex.rxjava3.core.Flowable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(viewModel: TrendingViewModel) {
    val state = viewModel.uiState.observeAsState().value!!
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            )
        },
        floatingActionButton = {
            if (state !is State.Loading)
                FloatingActionButton(
                    modifier = Modifier.size(Dimens.gu_6),
                    onClick = { viewModel.refresh() }
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Refresh),
                        contentDescription = null
                    )
                }
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = Dimens.gu_2)
            ) {
                state.apply {
                    when (this) {
                        State.Loading -> Loading()
                        State.NoData -> NoData()
                        is State.Success -> Success(trendingList)
                    }
                }
            }
        }
    )
}

@Composable
fun Loading() {
    val placeholderColor = TrendingTheme.colorSchema.viewPlaceholderBg
    LazyColumn(Modifier.customShimmer()) {
        items(20) {
            Spacer(Modifier.height(Dimens.gu_1_5))
            Row(Modifier.height(IntrinsicSize.Max)) {
                Image(
                    ColorPainter(placeholderColor),
                    contentDescription = null,
                    Modifier
                        .size(Dimens.gu_6)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(Dimens.gu_2))
                Column(
                    Modifier.fillMaxHeight(),
                    Arrangement.SpaceAround
                ) {
                    Spacer(
                        Modifier
                            .background(placeholderColor)
                            .fillMaxWidth()
                            .height(Dimens.gu_1_5)
                    )
                    Spacer(
                        Modifier
                            .background(placeholderColor)
                            .fillMaxWidth()
                            .height(Dimens.gu_1_5)
                    )
                }
            }
        }
    }
}

@Composable
fun Success(trendingList: List<Trending>) {
    LazyColumn(
        contentPadding = PaddingValues(top = Dimens.gu, bottom = Dimens.gu_7)
    ){
        itemsIndexed(
            trendingList,
            key = { _, item -> item.trendingId }
        ) { index, item ->
            TrendingView(item)
            if (index < trendingList.lastIndex)
                Divider(
                    thickness = Dimens.gu_0_125,
                    color = TrendingTheme.colorSchema.dividerColor
                )
        }
    }
}

@Composable
fun NoData() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.gu_6)
    )
}

@SystemUiPreview
@Composable
fun PreviewSuccess(){
    TrendingTheme {
        Success(trendingList = anyList(length = 3) { anyTrending() })
    }
}

@SystemUiPreview
@Composable
fun PreviewLoading(){
    TrendingTheme {
        Loading()
    }
}


@SystemUiPreview
@Composable
fun PreviewTrendingScreen(){
    val repository = object: Repository<List<Trending>, DataException>{
        override fun get(
            query: Query,
            operation: Operation
        ): Flowable<Either<DataException, List<Trending>>> = Flowable.just(Either.Right(anyList { anyTrending() }))

    }
    val  viewModel = TrendingViewModel(GetTrendingInteractor(repository))
    TrendingTheme {
        TrendingScreen(viewModel)
    }
}
