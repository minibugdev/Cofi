package com.omelan.cofi.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.omelan.cofi.R
import com.omelan.cofi.model.Step
import com.omelan.cofi.model.StepType
import com.omelan.cofi.ui.Spacing
import com.omelan.cofi.utils.toMillis
import com.omelan.cofi.utils.toStringDuration

enum class StepProgress { Current, Done, Upcoming }

@Composable
fun StepListItem(
    modifier: Modifier = Modifier,
    step: Step,
    stepProgress: StepProgress,
    onClick: ((Step) -> Unit)? = null
) {
    val constraintModifier = modifier
        .heightIn(min = 42.dp)
        .animateContentSize()
        .fillMaxWidth()
        .padding(vertical = Spacing.small)
        .clickable(
            onClick = { onClick?.let { it(step) } },
            enabled = onClick != null,
            role = Role.Button,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        )
    val painter = when (stepProgress) {
        StepProgress.Current -> painterResource(id = R.drawable.ic_play_arrow)
        StepProgress.Done -> painterResource(id = R.drawable.ic_check_circle)
        StepProgress.Upcoming -> painterResource(id = step.type.iconRes)
    }
    ConstraintLayout(
        modifier = constraintModifier
    ) {
        val (icon, name, valueAndTimeBox) = createRefs()
        Icon(
            painter = painter,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(icon) {
                    start.linkTo(parent.start, Spacing.small)
                    centerVerticallyTo(parent)
                }
        )

        Text(
            text = step.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(icon.end, Spacing.small)
                    centerVerticallyTo(parent)
                    end.linkTo(valueAndTimeBox.start, Spacing.small)
                    width = Dimension.fillToConstraints
                }
        )
        Row(
            Modifier.constrainAs(valueAndTimeBox) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        ) {
            if (step.value != null) {
                Text(
                    text = "${step.value}g",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = Spacing.small)

                )
            }
            if (step.time != null) {
                Text(
                    text = step.time.toStringDuration(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = Spacing.small)
                )
            }
        }
    }
}

@Preview
@Composable
fun StepListItemPreview() {
    StepListItem(
        step = Step(
            id = 0,
            name = "Somebody once told me the world is gonna roll me I ain't the sharpest " +
                "tool in the shed She was looking kind of dumb with her finger and her thumb " +
                "In the shape of an \"L\" on her forehead",
            time = 35.toMillis(),
            type = StepType.WATER,
            value = 60,
            orderInRecipe = 0,
        ),
        stepProgress = StepProgress.Current,
    )
}

@Preview
@Composable
fun StepListItemPreviewShort() {
    StepListItem(
        step = Step(
            id = 0,
            name = "Somebody once told",
            time = 35.toMillis(),
            type = StepType.WAIT,
            orderInRecipe = 0,
        ),
        stepProgress = StepProgress.Current,
    )
}