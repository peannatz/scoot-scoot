package com.example.scoot_scoot.android.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.ProfileBottomBar
import com.example.scoot_scoot.android.Data.HelpDataModel
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.HelpSectionViewModel
import kotlinx.coroutines.flow.forEach

object HelpScreen {

    val IntroString =
        "Welcome to the Scooter Rental App Help Center - Frequently Asked Questions!\n" +
                "\n" +
                "We're here to answer all your burning questions about the wild world of scooter rentals. \n" +
                "\n" +
                "Check out our FAQ section below to find the answers you seek:"

    val OutroString =
        "If you can't find the answer to your question in this FAQ section, don't hesitate to contact our support team. They'll be more than happy to assist you on your scooter rental adventures!"

    val popupVisible = mutableStateOf(false)

    @Composable
    fun HelpScreen(navController: NavController, hvm: HelpSectionViewModel = viewModel()) {

        val itemIds by hvm.itemIds.collectAsState()

        Scaffold(
            bottomBar = {
                BottomAppBar(modifier = Modifier.height(70.dp)) {
                    ProfileBottomBar.ProfileBottomBar(
                        navController
                    )
                }
            }) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(text = "FAQ", Modifier.padding(8.dp, vertical = 20.dp), fontSize = 100.sp)

                Text(
                    text = IntroString, Modifier.padding(8.dp, bottom = 20.dp), fontSize = 18.sp
                )
                LazyColumn() {
                    itemsIndexed(hvm.items.value) { index, item ->
                        CollapsableQuestion(
                            itemModel = item,
                            onClickItem = { hvm.onItemClicked(index) },
                            expanded = itemIds.contains(index)
                        )
                    }
                }

            }
            SupportButton(navController = navController)

            if (popupVisible.value) {
                Popup.Show(popupOpen = popupVisible)
            }

        }

    }

    @Composable
    fun SupportButton(navController: NavController) {
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            FloatingActionButton(
                onClick = {
                    popupVisible.value = true
                },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .offset((-30).dp, 30.dp)
                    .size(75.dp)
                    .align(Alignment.TopEnd)
            ) {
                Text(text = "Contact Support", textAlign = TextAlign.Center)
            }
        }
    }

    @Composable
    fun CollapsableQuestion(
        itemModel: HelpDataModel,
        onClickItem: () -> Unit,
        expanded: Boolean
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
        ) {
            Column {
                ClickableQuestion(questionText = itemModel.question, onClickItem = onClickItem)
                Spacer(modifier = Modifier.size(1.dp))
                ExpandableAnswer(answerText = itemModel.answer, isExpanded = expanded)
            }
        }
    }

    @Composable
    fun ClickableQuestion(questionText: String, onClickItem: () -> Unit) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colors.primary)
                .clickable(
                    indication = null, // Removes the ripple effect on tap
                    interactionSource = remember { MutableInteractionSource() }, // Removes the ripple effect on tap
                    onClick = onClickItem
                )
                .padding(8.dp)

        ) {
            Text(
                text = questionText,
                fontSize = 17.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    fun ExpandableAnswer(answerText: AnnotatedString, isExpanded: Boolean) {
        // Opening Animation
        val expandTransition = remember {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(300)
            ) + fadeIn(
                animationSpec = tween(300)
            )
        }

        // Closing Animation
        val collapseTransition = remember {
            shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(300)
            ) + fadeOut(
                animationSpec = tween(300)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandTransition,
            exit = collapseTransition
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .padding(15.dp)
            ) {
                Text(
                    text = answerText,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
