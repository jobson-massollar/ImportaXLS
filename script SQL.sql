DROP TABLE departamentos;

CREATE TABLE departamentos (
	id uuid NOT NULL,
	depto varchar(5) NOT NULL,
	nome varchar(100) NOT NULL,
	CONSTRAINT departamentos_depto_unique UNIQUE (depto),
	CONSTRAINT departamentos_pkey PRIMARY KEY (id)
);

/*-------------------------------------------------------------------*/

DROP TABLE alunos;

CREATE TABLE alunos (
	id uuid NOT NULL,
	matricula varchar(14) NOT NULL,
	nome varchar(100) NOT NULL,
	sexo bpchar(1) NOT NULL,
	dt_nasc date NULL,
	versao varchar(6) NOT NULL,
	logradouro varchar(100) NOT NULL,
	numero varchar(10) NOT NULL,
	complemento varchar(60) NOT NULL,
	bairro varchar(60) NOT NULL,
	cidade varchar(60) NOT NULL,
	cep varchar(10) NOT NULL,
	telefone1 varchar(20) NOT NULL,
	telefone2 varchar(20) NOT NULL,
	email varchar(40) DEFAULT ''::character varying NOT NULL,
	ingresso varchar(100) NOT NULL,
	evasao varchar(100) NOT NULL,
	dt_evasao date NULL,
	CONSTRAINT alunos_matricula_unique UNIQUE (matricula),
	CONSTRAINT alunos_pkey PRIMARY KEY (id)
);

/*-------------------------------------------------------------------*/

DROP TABLE disciplinas;

CREATE TABLE disciplinas (
	id uuid NOT NULL,
	versao varchar(6) NOT NULL,
	codigo varchar(10) NOT NULL,
	nome varchar(100) NOT NULL,
	periodo int4 NOT NULL,
	creditos int4 NOT NULL,
	horas int4 NOT NULL,
	tipo varchar(60) NOT NULL,
	situacao varchar(20) NOT NULL,
	aula varchar(50) NOT NULL,
	CONSTRAINT disciplinas_pkey PRIMARY KEY (id),
	CONSTRAINT idx_disciplina_codigo_versao_aula UNIQUE (codigo, versao, aula)
);

CREATE INDEX disciplinas_codigo ON public.disciplinas USING btree (codigo);

/*-------------------------------------------------------------------*/

DROP TABLE inscricoes;

CREATE TABLE inscricoes (
	id uuid NOT NULL,
	matricula varchar(14) NOT NULL,
	codigo varchar(10) NOT NULL,
	turma varchar(10) NOT NULL,
	situacao int4 NOT NULL,
	descricao varchar(50) NOT NULL,
	ano int4 NOT NULL,
	periodo int4 NOT NULL,
	dt_solicitacao date NOT NULL,
	hora_solicitacao time NOT NULL,
	dt_processamento date NULL,
	CONSTRAINT inscricoes_pkey PRIMARY KEY (id)
);
CREATE INDEX inscricoes_codigo ON public.inscricoes USING btree (codigo);
CREATE INDEX inscricoes_descricao ON public.inscricoes USING btree (descricao);
CREATE INDEX inscricoes_matricula ON public.inscricoes USING btree (matricula);
CREATE INDEX inscricoes_situacao ON public.inscricoes USING btree (situacao);

/*-------------------------------------------------------------------*/

DROP TABLE itens_diario;

CREATE TABLE itens_diario (
	id uuid NOT NULL,
	matricula varchar(14) NOT NULL,
	nome varchar(100) NOT NULL,
	curso int4 NOT NULL,
	depto varchar(5) NOT NULL,
	codigo varchar(10) NOT NULL,
	versao varchar(6) NOT NULL,
	turma varchar(20) NOT NULL,
	CONSTRAINT itens_diario_pkey PRIMARY KEY (id)
);
CREATE INDEX itens_diario_codigo ON public.itens_diario USING btree (codigo);
CREATE INDEX itens_diario_matricula ON public.itens_diario USING btree (matricula);

/*-------------------------------------------------------------------*/

DROP TABLE itens_historico;

CREATE TABLE public.itens_historico (
	id uuid NOT NULL,
	matricula varchar(14) NOT NULL,
	ano int4 NOT NULL,
	periodo int4 NOT NULL,
	desc_periodo varchar(20) NOT NULL,
	versao varchar(6) NOT NULL,
	codigo varchar(10) NOT NULL,
	nome varchar(200) NOT NULL,
	situacao int4 NOT NULL,
	descricao varchar(50) NOT NULL,
	nota float4 NULL,
	creditos int4 NOT NULL,
	horas int4 NOT NULL,
	CONSTRAINT itens_historico_pkey PRIMARY KEY (id)
);
CREATE INDEX itens_historico_codigo ON public.itens_historico USING btree (codigo);
CREATE INDEX itens_historico_matricula ON public.itens_historico USING btree (matricula);
CREATE INDEX itens_historico_versao_codigo_idx ON public.itens_historico USING btree (versao, codigo);

/*-------------------------------------------------------------------*/

drop view vw_disciplinas;

create view vw_disciplinas as
select versao, codigo, nome, tipo, sum(horas) as horas, sum(creditos) as creditos
from disciplinas
group by versao, codigo, nome, tipo;

/*-------------------------------------------------------------------*/

drop view vw_alunos_ativos;

create view vw_alunos_ativos as
select *
from alunos
where dt_evasao is null and left(evasao, 3) <> 'ABA';

/*-------------------------------------------------------------------*/

drop view vw_itens_historico;

create view vw_itens_historico as
select h.*, 
case
  when h.versao = '2023/2' and left(h.codigo, 3) = 'ATC' then 'Complementar' 
  when d1.tipo is not null then d1.tipo 
  when d2.tipo is not null then 'Antiga' 
  else null 
end as tipo
from itens_historico h
left join vw_disciplinas d1 on h.versao = d1.versao and h.codigo = d1.codigo
left join vw_disciplinas d2 on d2.versao = case when h.versao = '2023/2' then '2008/1' else null end and h.codigo = d2.codigo;

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
with
HISTORICO as (
select h.*
from vw_itens_historico h
inner join vw_alunos_ativos a on a.matricula = h.matricula
left join vw_disciplinas d on h.versao = d.versao  and h.codigo = d.codigo
where (h.situacao = 1 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11)
)
select a.matricula, a.versao,
coalesce(T1.qtd,0)+coalesce(T2.qtd,0) as qtd_obrig, coalesce(T1.horas,0)+coalesce(T2.horas,0)  as horas_obrig, 
coalesce(T3.qtd,0) as qtd_opt, coalesce(T3.horas,0) as horas_opt,
coalesce(T4.qtd,0) as qtd_elet, coalesce(T4.horas,0) as horas_elet,
coalesce(T2.qtd,0) as qtd_compl, coalesce(T2.horas,0) as horas_compl,
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
from HISTORICO h
where h.tipo = 'Obrigatória'
group by h.matricula) T1 on a.matricula = T1.matricula
left join
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from HISTORICO h
where h.tipo = 'Complementar'
group by h.matricula) T2 on a.matricula = T2.matricula
left join 
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from HISTORICO h
where h.tipo = 'Optativa'
group by h.matricula) T3 on a.matricula = T3.matricula
left join
(select h.matricula, count(*) as qtd, sum(h.horas) as horas
from HISTORICO h
where h.tipo is null
group by h.matricula) T4 on a.matricula = T4.matricula
left join
(select a.matricula, count(*) as qtd
from vw_disciplinas d
inner join vw_alunos_ativos a on a.versao = d.versao and d.tipo = 'Obrigatória'
left join (
select h.*
from vw_itens_historico h
where (h.situacao = 1 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11) 
and h.tipo = 'Obrigatória') T on a.matricula = T.matricula and d.versao = T.versao and d.codigo = T.codigo
where T.codigo is null
group by a.matricula) T5 on a.matricula = T5.matricula
order by a.matricula;

/*----------------------------------------------------------------------------------
 Lista de matrículas e disciplinas obrigatórias que ainda faltam ser cursadas
 ----------------------------------------------------------------------------------*/
select a.matricula, a.nome, d.*
from vw_disciplinas d
inner join vw_alunos_ativos a on a.versao = d.versao and d.tipo = 'Obrigatória'
left join (
select h.*
from vw_itens_historico h
where (h.situacao = 1 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11) 
and h.tipo = 'Obrigatória') T on a.matricula = T.matricula and d.versao = T.versao and d.codigo = T.codigo
where T.codigo is null
order by a.versao, a.nome;

/*----------------------------------------------------------------------------------
 Lista de matrículas e qtd de disciplinas obrigatórias que ainda faltam ser cursadas
 ----------------------------------------------------------------------------------*/
select a.matricula, count(*) as qtd
from vw_disciplinas d
inner join vw_alunos_ativos a on a.versao = d.versao and d.tipo = 'Obrigatória'
left join (
select h.*
from vw_itens_historico h
where (h.situacao = 1 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11) 
and h.tipo = 'Obrigatória') T on a.matricula = T.matricula and d.versao = T.versao and d.codigo = T.codigo
where T.codigo is null
group by a.matricula;

---------------------

with
HISTORICO as (
select h.*
from vw_itens_historico h
inner join vw_alunos_ativos a on a.matricula = h.matricula
left join vw_disciplinas d on h.versao = d.versao  and h.codigo = d.codigo
where (h.situacao = 1 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11)
)
select * from (
select h.matricula, h.codigo, h.nome, h.horas, row_number() over (partition by h.matricula order by h.horas desc) as seq
from HISTORICO h
where h.tipo is null) T 
where T.seq <= 2
order by T.matricula, T.seq
