/*----------------------------------------------------------------------------------
 Alunos inscritos em disciplinas diferentes da versão do seu currículo
 ----------------------------------------------------------------------------------*/
select i.*, a.matricula, a.nome, a.versao, d.versao
from itens_historico i
         inner join alunos a on a.matricula = i.matricula
         left join disciplinas d on i.codigo = d.codigo and a.versao = d.versao
where i.ano  = 2026 and i.periodo = 1 and d.versao is null and i.codigo like 'TIN%'
order by i.codigo;

/*----------------------------------------------------------------------------------
 Total de alunos inscritos nas disciplinas do DIA (alunos do BSI e de outros)
 ----------------------------------------------------------------------------------*/

select i.versao, i.codigo, i.turma, d.nome, count(*) as qtd
from itens_diario i
inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
where i.depto='DIA'
group by i.versao, i.codigo, i.turma, d.nome
order by i.turma asc, i.versao desc;

select i.versao, i.codigo, i.turma, d.nome, A.qtd + coalesce(B.qtd, 0) as total, A.qtd as bsi, coalesce(B.qtd, 0) as outros
from itens_diario i
         inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
         inner join (
    select i.versao, i.codigo, i.turma, count(*) as qtd
    from itens_diario i
             inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
    where i.depto='DIA' and substring(i.matricula, 6, 3) = '210'
    group by i.versao, i.codigo, i.turma
) A on i.versao = A.versao and i.codigo = A.codigo and i.turma = A.turma
         left join (
    select i.versao, i.codigo, i.turma, count(*) as qtd
    from itens_diario i
             inner join vw_disciplinas d on i.versao = d.versao and i.codigo = d.codigo
    where i.depto='DIA' and substring(i.matricula, 6, 3) <> '210'
    group by i.versao, i.codigo, i.turma
) B on i.versao = B.versao and i.codigo = B.codigo and i.turma = B.turma
where i.depto='DIA'
group by i.versao, i.codigo, i.turma, d.nome, A.qtd, B.qtd
order by i.turma asc, i.versao desc;

/*----------------------------------------------------------------------------------
 Total de alunos inscritos nas disciplinas do BSI (alunos do BSI e de outros)
 ----------------------------------------------------------------------------------*/
select i.versao, i.codigo, i.turma, i.nome_disciplina, i.depto, A.qtd + coalesce(B.qtd, 0) as total, A.qtd as bsi, coalesce(B.qtd, 0) as outros
from vw_itens_diario i
inner join (
    select i.versao, i.codigo, i.turma, count(*) as qtd
    from vw_itens_diario i
    where substring(i.matricula, 6, 3) = '210'
    group by i.versao, i.codigo, i.turma
) A on i.versao = A.versao and i.codigo = A.codigo and i.turma = A.turma
left join (
    select i.versao, i.codigo, i.turma, count(*) as qtd
    from vw_itens_diario i
    where substring(i.matricula, 6, 3) <> '210'
    group by i.versao, i.codigo, i.turma
) B on i.versao = B.versao and i.codigo = B.codigo and i.turma = B.turma
group by i.versao, i.codigo, i.turma, i.nome_disciplina, i.depto, A.qtd, B.qtd
order by i.turma asc, i.versao desc;

/*----------------------------------------------------------------------------------
 Alunos ativos e qtd de matrículas em disciplinas no período/ano
 ----------------------------------------------------------------------------------*/
select a.matricula, a.nome, a.email,
case
   when i.matricula is null then 0
   else count(*)
end as qtd
from vw_alunos_ativos a
left join itens_historico i on a.matricula = i.matricula
where i.ano = 2026 and i.periodo = 1
group by a.matricula, a.nome, a.email, i.matricula
order by qtd, a.matricula;

/*----------------------------------------------------------------------------------
 Alunos ativos e quantidades de trancamentos
 ----------------------------------------------------------------------------------*/
select a.matricula, a.nome, a.email,
case
   when i.matricula is null then 0
   else count(*)
end as qtd
from vw_alunos_ativos a
left join (
    select * from itens_historico where codigo = 'TRT0001'
) i on a.matricula = i.matricula
group by a.matricula, a.nome, a.email, i.matricula
order by qtd desc, a.matricula;

/*----------------------------------------------------------------------------------
 Lista de alunos de uma disciplina (independente de turma)
 ----------------------------------------------------------------------------------*/
select a.* from
vw_alunos_ativos a
inner join itens_diario i on a.matricula = i.matricula and i.codigo = 'TIN0297'
order by a.nome;

/*----------------------------------------------------------------------------------
 Lista de alunos de uma turma (independente da disciplina)
 ----------------------------------------------------------------------------------*/
select a.* from
vw_alunos_ativos a
inner join itens_diario i on a.matricula = i.matricula and i.turma = 'BSI.ED'
order by a.nome;

select a.* from
vw_alunos_ativos a
inner join itens_diario i on a.matricula = i.matricula and i.turma = (select distinct turma from itens_diario  where codigo = 'TIN0225')
order by a.nome;

/*----------------------------------------------------------------------------------
 Lista de alunos de um currículo (que ainda não concluíram)
 ----------------------------------------------------------------------------------*/

select a.* from
vw_alunos_ativos a
where a.versao = '2008/1';

select a.* from
vw_alunos_ativos a
where a.versao = '2023/2';

-- Quem migrou em quem entrou na grade 2023.2
select a.* from
vw_alunos_ativos a
where a.versao = '2023/2' and left(a.matricula,5) < '20232';

select a.* from
vw_alunos_ativos a
where a.versao = '2023/2' and left(a.matricula,5) >= '20232';

/*----------------------------------------------------------------------------------
 Quantidade de alunos em cada currículo (que ainda não concluíram)
 ----------------------------------------------------------------------------------*/
select T1.cv2008, T2.cv2023, T1.cv2008+T2.cv2023 as total
from (select count(*) as cv2008
from vw_alunos_ativos a
where a.versao = '2008/1') T1
cross join
(select count(*) as cv2023
from vw_alunos_ativos a
where a.versao = '2023/2') T2;

/*----------------------------------------------------------------------------------
 Alunos da grade antiga que ainda estão devendo uma determinada disciplina
 (não estão aprovados e não tiveram dispensa)
 ----------------------------------------------------------------------------------*/
select a.* from
vw_alunos_ativos a
left join
(
select a.matricula from
alunos a
inner join itens_historico h on 
h.matricula  = a.matricula and 
h.codigo = 'TIN0117' and 
(h.situacao = 1 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11)
where a.versao = '2008/1' and dt_evasao is null
) T on a.matricula = T.matricula 
where a.versao = '2008/1' and dt_evasao is null and T.matricula is null;

/*----------------------------------------------------------------------------------
 Disciplinas que mantiveram o código do currículo antigo para o novo
 ----------------------------------------------------------------------------------*/
select d.* 
from vw_disciplinas d
inner join (
select d.codigo, count(*)
from vw_disciplinas d
group by d.codigo
having count(*) > 1) T on d.codigo = T.codigo
order by d.codigo;

/*----------------------------------------------------------------------------------
 Lista de matrícula e quantitativo/horas de obrigatórias, optativas e eletivas
 (somente alunos ativos)
 ----------------------------------------------------------------------------------*/
select a.matricula, a.versao,
coalesce(T1.qtd,0),-- +coalesce(T2.qtd,0) as qtd_obrig, 
coalesce(T1.horas,0),-- +coalesce(T2.horas,0)  as horas_obrig, 
coalesce(T3.qtd,0) as qtd_opt, 
coalesce(T3.horas,0) as horas_opt,
coalesce(T4.qtd,0) as qtd_elet, 
coalesce(T4.horas,0) as horas_elet,
coalesce(T2.qtd,0) as qtd_compl, 
coalesce(T2.horas,0) as horas_compl,
coalesce(T1.horas,0)+coalesce(T2.horas,0)+coalesce(T3.horas,0) as total,
coalesce(T5.qtd,0) as obrig_faltantes
--case
--  when T3.horas > 0 and T3.qtd >= 2 then 600 - ((T3.horas / T3.qtd) * 2 + coalesce(T2.horas, 0))
--  when T3.horas > 0 and T3.qtd = 1  then 600 - (T3.horas + coalesce(T2.horas, 0))
--  else 600 - coalesce(T2.horas, 0)
--end as opt_faltantes
from vw_alunos_ativos a
left join
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from vw_disciplinas_cursadas h
where h.tipo = 'Obrigatória'
group by h.matricula) T1 on a.matricula = T1.matricula
left join
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from vw_disciplinas_cursadas h
where h.tipo = 'Complementar'
group by h.matricula) T2 on a.matricula = T2.matricula
left join 
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from vw_disciplinas_cursadas h
where h.tipo = 'Optativa'
group by h.matricula) T3 on a.matricula = T3.matricula
left join
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from vw_disciplinas_cursadas h
where h.tipo is null
group by h.matricula) T4 on a.matricula = T4.matricula
left join
(select obf.matricula, count(*) as qtd
from vw_obrigatorias_faltantes obf
group by obf.matricula) T5 on a.matricula = T5.matricula
order by a.matricula;

/*
Lista de alunos e eletivas cursadas (ordenadas por horas)
Quantidade de eletivas que podem ser usadas como optativas (grade 2023/2)
*/
select T.matricula, count(*) as qtd, sum(T.horas) as total_horas
from (
select vdc.*, row_number() over(partition by vdc.matricula order by vdc.matricula asc, vdc.horas desc) as sequencia
from vw_disciplinas_cursadas vdc
where vdc.tipo ='Eletiva'  and (vdc.situacao = 1 or vdc.situacao = 8 or vdc.situacao = 7)) T
where T.sequencia <= 2
group by T.matricula

/*
Alunos que cursaram CD1 ou CD2 na EP
*/
select vih.*
from vw_itens_historico vih 
inner join vw_alunos_ativos vaa on vih.matricula = vaa.matricula
where (vih.codigo = 'TMT0001' or vih.codigo = 'TMT0002') and (vih.situacao = 1 or vih.situacao = 7 or vih.situacao = 8 or vih.situacao = 11)
order by vih.matricula

/*
Jubilamento:
- Aluno sem inscrições no período atual E 4 trancamentos (ABANDONO)
- Limites: 12 períodos + trancamentos + extensão + pandemia
-   16 períodos sem extensão
-   17 períodos com 1 período de extensão
-   18 períodos com 2 períodos de extensão
- Aluno não formado onde período atual > período limite (PRAZO)
*/

/*
HorasEletivas(eletivasCursadas, eletivasMatriculadas)
cursadas = pegar máximo 2 com as maiores cargas horárias de eletivasCursadas

se cursadas.qtd = 2 e cursadas.horas >= 120
  retorna 120
  
total = eletivasCursadas + eletivasMatriculadas

validas = pegar máximo 2 com as maiores cargas horárias de total

retornar max(120, validas.horas)
*/

/*
Grade 2023.2
IOB IOP IEL / NOB HOP ELC / TOB

Formandos: NOB >= TOB && (HOP >= 600 || (HOP >= 480 && HOP + HorasEletivas(ELC, IEL) >= 600))

Inscrições OK
- Matriculadas >= 3
- Formado: OK
- Formando: OK
- 1 ou 2: NOB + IOB >= TOB && (HOP + IOP + horasEletivas(HEL, IEL) >= 600)

Inscrições NÃO OK
- Matriculadas < 3
- Não Formado
- Não formando: NOB + IOB < TOB || (HOP + IOP + horasEletivas(HEL, IEL) < 600)
*/

