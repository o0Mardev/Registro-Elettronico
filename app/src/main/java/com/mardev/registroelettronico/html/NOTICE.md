# Notice
#### This code in the html package has been modified from the https://github.com/ireward/compose-html repository on GitHub. It is used because the Axios API returns html as a response and Jetpack Compose does not support it.
#### So I tried two ways:
1. Clean the response using Jsoup and display the clean response as plain text.
   This has a disadvantage, when for example in communications we have html tags like "table", "p", "b" we cannot display them correctly.

2. Leave the response as it is and create the logic to interpret the tags and display them accordingly in the Composable. The disadvantage of this solution is that every time the composable is recomposed, all this logic is executed again, making the composition slower.