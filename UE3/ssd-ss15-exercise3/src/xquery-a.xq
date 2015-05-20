let $out := doc("../resources/jeopardy.xml")//game
let $in := doc("../resources/jeopardy.xml")//answer

return
<correct>{

for $i in $out/asked/givenanswer
for $j in $in
where $i/@player="Bart" and $j/@correct="yes" and $i=$j and $i/../../@session="abcd"
return <givenanswer player="Bart">{data($i)}</givenanswer>
}</correct>