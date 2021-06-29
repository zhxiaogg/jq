

// Define a grammar called Hello
grammar SQL;

sql: select_stmt;
select_stmt: SELECT_ result_column (COMMA result_column)* (
                         FROM_ (table_or_subquery (COMMA table_or_subquery)* | join_clause)
                     )? (WHERE_ expr)? (GROUP_ BY_ expr (COMMA expr)* (HAVING_ expr)?)?;

result_column: STAR | table_name DOT STAR | expr;

table_or_subquery:  table_name (AS_? table_alias)?
    | OPEN_PAR select_stmt CLOSE_PAR (AS_? table_alias)?
;

join_clause: table_or_subquery (join_operator table_or_subquery join_constraint?)* ;

join_constraint: ON_ expr;

join_operator:
    COMMA
    | NATURAL_? (LEFT_ OUTER_? | INNER_ | CROSS_)? JOIN_
;

expr: literal_value
    | (table_name DOT)? column_name
    | unary_operator expr
    | expr ( STAR | DIV | MOD) expr
    | expr ( PLUS | MINUS) expr
    | expr ( LT | LT_EQ | GT | GT_EQ) expr
    | expr ( ASSIGN | EQ | NOT_EQ1 | NOT_EQ2 | LIKE_ ) expr
    | expr AND_ expr
    | expr OR_ expr
    | expr NOT_? BETWEEN_ expr AND_ expr
    | expr NOT_? IN_ ( OPEN_PAR (select_stmt | expr ( COMMA expr)*)? CLOSE_PAR );

unary_operator: MINUS | PLUS | NOT_ ;

literal_value:
    NUMERIC_LITERAL
    | STRING_LITERAL
    | NULL_
    | TRUE_
    | FALSE_
;

table_name: any_name;
column_name: any_name;
table_alias: any_name;

any_name: IDENTIFIER| STRING_LITERAL;

/* lexer */
SCOL:      ';';
DOT:       '.';
OPEN_PAR:  '(';
CLOSE_PAR: ')';
COMMA:     ',';
ASSIGN:    '=';
STAR:      '*';
PLUS:      '+';
MINUS:     '-';
TILDE:     '~';
PIPE2:     '||';
DIV:       '/';
MOD:       '%';
LT2:       '<<';
GT2:       '>>';
AMP:       '&';
PIPE:      '|';
LT:        '<';
LT_EQ:     '<=';
GT:        '>';
GT_EQ:     '>=';
EQ:        '==';
NOT_EQ1:   '!=';
NOT_EQ2:   '<>';



IN_:                I N;
LIKE_:              L I K E;
AND_:               A N D;
OR_:                O R;
NOT_:               N O T;
BETWEEN_:           B E T W E E N;
NULL_:              N U L L;
TRUE_:              T R U E;
FALSE_:             F A L S E;
SELECT_:            S E L E C T;
FROM_:              F R O M;
WHERE_:             W H E R E;
GROUP_:             G R O U P;
BY_:                B Y;
HAVING_:            H A V I N G;
AS_:                A S;
ON_:                O N;
NATURAL_:           N A T U R A L;
LEFT_:              L E F T;
OUTER_:             O U T E R;
INNER_:             I N N E R;
CROSS_:             C R O S S;
JOIN_:              J O I N;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

IDENTIFIER:
    '"' (~'"' | '""')* '"'
    | '`' (~'`' | '``')* '`'
    | '[' ~']'* ']'
    | [a-zA-Z_] [a-zA-Z_0-9]*;

NUMERIC_LITERAL: ((DIGIT+ ('.' DIGIT*)?) | ('.' DIGIT+)) (E [-+]? DIGIT+)? | '0x' HEX_DIGIT+;
BOOLEAN_LITERAL: 'true'|'false';
STRING_LITERAL: '\'' ( ~'\'' | '\'\'')* '\'';

fragment HEX_DIGIT: [0-9a-fA-F];
fragment DIGIT:     [0-9];

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];
