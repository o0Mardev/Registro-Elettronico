package com.mardev.registroelettronico.feature_main.presentation.components.note_screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mardev.registroelettronico.feature_main.presentation.components.common.DateItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreen(
    state: NoteScreenState,
    modifier: Modifier = Modifier
) {
    val groupedNotes = state.filteredNotes.groupBy { it.date }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.height(8.dp)) {
                if (state.loading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = modifier.padding(top = paddingValues.calculateTopPadding(), start = 2.dp, end = 2.dp).then(
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .windowInsetsPadding(WindowInsets.displayCutout)
            } else Modifier)) {
            if (groupedNotes.isEmpty()) {
                item {
                    Text(text = "Non sono presenti note per il seguente periodo.", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            } else {
                groupedNotes.forEach { (header, items) ->
                    stickyHeader {
                        DateItem(
                            date = header,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    items(items, key = { item -> item.id }) { note ->
                        Spacer(modifier = Modifier.height(4.dp))
                        NoteItem(note = note, showDate = false)
                        Spacer(modifier = Modifier.height(4.dp))
                        HorizontalDivider(thickness = DividerDefaults.Thickness.times(2))
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    )
                }
            }
        }
    }
}
