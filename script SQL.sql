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

DROP TABLE pre_requisitos;

CREATE TABLE pre_requisitos (
	id uuid NOT NULL,
	versao varchar(6) NOT NULL,
	codigo varchar(10) NOT NULL,
	codigo_pre_req varchar(10) NOT NULL,
	CONSTRAINT pre_requisitos_pkey PRIMARY KEY (id),
	CONSTRAINT idx_pre_requisitos_versao_codigo_codigo_pre_req UNIQUE (versao, codigo, codigo_pre_req)
);

CREATE INDEX pre_requisitos_versao_codigo ON public.pre_requisitos USING btree (versao, codigo);

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

DROP TABLE extensoes_prazo;

CREATE TABLE public.extensoes_prazo (
	id uuid NOT NULL,
	matricula varchar(14) NOT NULL,
	prazo int4 NOT NULL,
	CONSTRAINT extensoes_prazo_matricula_unique UNIQUE (matricula),
	CONSTRAINT extensoes_prazo_pkey PRIMARY KEY (id)
);

/*-------------------------------------------------------------------*/

DROP TABLE disciplinas_equivalentes;

CREATE TABLE public.disciplinas_equivalentes (
	id uuid NOT NULL,
	versao varchar(6) NOT NULL,
	codigo varchar(10) NOT NULL,
	nome varchar(200) NOT NULL,
	CONSTRAINT disciplinas_equivalentes_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_disciplinas_equivalentes_versao_codigo ON disciplinas_equivalentes (versao, codigo);

insert into disciplinas_equivalentes(id, versao, codigo, nome) values('00b9cfcf-2083-44a5-bced-a9b1afe67db0', '2023/2', 'TMT001', 'CÁLCULO-I');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('a32335a2-7d4f-4352-8184-3f54ae075bf6', '2023/2', 'TMT002', 'CÁLCULO II');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('4b360a80-4df2-4173-b656-70e501a81470', '2023/2', 'TMT005', 'CÁLCULO-0');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('3fd977a1-8a69-4d88-b456-8901fbf56a92', '2023/2', 'TMT015', 'CÁLCULO-2');
insert into disciplinas_equivalentes(id, versao, codigo, nome) values('cbc50c52-91f2-4078-8d68-43fddb2b3d41', '2023/2', 'TIN0202', 'PROGRAMAÇÃO II');

/*----------------------------------------------------------------------------------
 Disciplinas únicas com somatório de horas e créditos
 Essa view permite somar as horas de teoria e prática das disciplinas
 ----------------------------------------------------------------------------------*/

drop view vw_disciplinas;

create or replace view vw_disciplinas as
select versao, codigo, nome, periodo, sum(creditos) as creditos, sum(horas) as horas, tipo
from disciplinas
group by versao, codigo, nome, periodo, tipo;

/*----------------------------------------------------------------------------------
 Alunos com prazo de extensão e trancamentos
 ----------------------------------------------------------------------------------*/

drop view vw_alunos;

create or replace view vw_alunos as
select a.*, coalesce(T1.trancamentos, 0) as trancamentos, coalesce(T2.prazo, 0) as prazo_extensao
from alunos a
left join (
    select ih.matricula, count(*) as trancamentos
	from itens_historico ih 
	where codigo = 'TRT0001'
	group by ih.matricula
) T1 on T1.matricula = a.matricula
left join extensoes_prazo T2 on T2.matricula = a.matricula;

/*----------------------------------------------------------------------------------
 Alunos com matrícula ativa
 ----------------------------------------------------------------------------------*/

drop view vw_alunos_ativos;

create or replace view vw_alunos_ativos as
select va.*
from vw_alunos va
where va.dt_evasao is null and left(va.evasao, 3) <> 'ABA';

/*----------------------------------------------------------------------------------
 Histórico com o tipo da disciplina:
 Obrigatória
 Complementar
 Eletiva
 Antiga - obrigatória ou optativa da grade antiga (disciplina cursada na grade antiga)
 ----------------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------------
 Alunos ativos e disciplinas cursadas
 ----------------------------------------------------------------------------------*/

drop view vw_disciplinas_cursadas;

create or replace view vw_disciplinas_cursadas as
select h.*
from itens_historico h
inner join vw_alunos_ativos a on a.matricula = h.matricula
left join vw_disciplinas d on h.versao = d.versao  and h.codigo = d.codigo
where (h.situacao = 1 or h.situacao = 4 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11);

/*----------------------------------------------------------------------------------
 Alunos e disciplinas obrigatórias faltantes
 ----------------------------------------------------------------------------------*/

drop view vw_obrigatorias_faltantes;

-- create or replace view vw_obrigatorias_faltantes as
-- select a.matricula, d.*
-- from vw_disciplinas d
-- inner join vw_alunos_ativos a on a.versao = d.versao and d.tipo = 'Obrigatória'
-- left join (
-- select vdc.*
-- from vw_disciplinas_cursadas vdc
-- where vdc.tipo = 'Obrigatória') T on a.matricula = T.matricula and d.versao = T.versao and d.codigo = T.codigo
-- where T.codigo is null;

create or replace view vw_obrigatorias_faltantes as
select a.matricula, d.*
from vw_disciplinas d
inner join vw_alunos_ativos a on a.versao = d.versao and d.tipo = 'Obrigatória'
left join (
select h.*
from itens_historico h
inner join vw_alunos_ativos a on a.matricula = h.matricula
left join vw_disciplinas d on h.versao = d.versao  and h.codigo = d.codigo
where (h.situacao = 1 or h.situacao = 4 or h.situacao = 7 or h.situacao = 8 or h.situacao = 11) and 
      h.tipo = 'Obrigatória') T on a.matricula = T.matricula and d.versao = T.versao and d.codigo = T.codigo
where T.codigo is null;


/*-------------------------------------------------------------------*/

update itens_historico ih set horas = 90 where ih.codigo = 'ATC0021';
update itens_historico ih set horas = 180 where ih.codigo = 'ATC0010';
update itens_historico ih set horas = 45 where ih.codigo = 'ATC0031';
update itens_historico ih set horas = 60 where ih.codigo = 'ATC0100';

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
- Aluno sem inscrições no período atual com 4 trancamentos (ABANDONO)
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

