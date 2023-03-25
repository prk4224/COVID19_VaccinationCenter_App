package com.jaehong.presentation.ui.map

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehong.domain.model.CenterItem

@Composable
fun BottomSheetScreen(
    selectedItem: CenterItem?,
    modifier: Modifier,
) {
    AnimatedVisibility(
        visible = selectedItem != null,
        modifier = modifier,
        enter = slideInVertically (initialOffsetY = {
            +it
        }),
        exit = slideOutVertically(targetOffsetY = {
            +it
        }),
    ) {
        Box(modifier = modifier
            .height(200.dp)
            .background(Color.White)
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(topEndPercent = 10, topStartPercent = 10)
            )
        ) {
            if(selectedItem != null){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 30.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "주소: ${selectedItem.address}",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "센터 이름: ${selectedItem.centerName}",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "시설 이름: ${selectedItem.facilityName}",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "전화 번호: ${selectedItem.phoneNumber}",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(text = "수정일시: ${selectedItem.updatedAt}")
                }
            }
        }
    }
}