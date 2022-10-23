package com.mecofarid.trending.features.repo.ui.view

import com.mecofarid.trending.features.repo.domain.model.Repo

data class RepoView(val repo: Repo, var isExpanded: Boolean = false)
