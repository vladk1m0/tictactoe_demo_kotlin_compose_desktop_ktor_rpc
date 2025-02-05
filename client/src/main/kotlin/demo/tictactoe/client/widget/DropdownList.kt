package demo.tictactoe.client.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a dropdown list for selecting a player symbol.
 *
 * @param onSelectionChanged A callback function that is called when the selected option changes.
 * @param selectedOption The initially selected option.
 */
@Composable
fun DropdownList(
    onSelectionChanged: (String) -> Unit = {},
    selectedOption: String?
) {
    val options = listOf("X", "O")
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val (selectedOptionText, setSelectedOptionText) = remember { mutableStateOf(selectedOption ?: options[0]) }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(350.dp, 32.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
            .clickable { setExpanded(!expanded) },
    ) {
        Text(
            text = selectedOptionText,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 10.dp)
        )
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        setSelectedOptionText(selectionOption)
                        setExpanded(false)
                        onSelectionChanged(selectionOption)
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}
