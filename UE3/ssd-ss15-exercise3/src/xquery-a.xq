<stats>{
    for $z in doc("../resources/jeopardy.xml")//game
        order by $z/@session
        return <session id="{$z/@session}">{
            for $x in $z/player/@ref
                return <correct player="{$x}">{count(
                    for $w in doc("../resources/jeopardy.xml")//answer
                        for $y in $z/asked/givenanswer
                            where $y=$w and $y/@player=$x and $w/@correct="yes"
                            return <pl>{data($y/asked/givenanswer)}</pl>
                ), "&#10;"}</correct>
        }</session>
    }
</stats>