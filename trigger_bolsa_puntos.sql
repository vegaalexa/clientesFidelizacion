create or replace function F_TR_POBLAR_BOLSA_PUNTOS1() returns Trigger
as
$$
declare
	vd_fecha_asig_bp date:= new.fecha_asignacion_puntaje;
	vd_fecha_caducidad date;
	nv_cont_reg_venc integer:=0;
	nv_monto_compra bigint:=new.monto_operacion;
	nv_puntaje_asignado integer:=0;
	nv_cont_regla integer:=0;
	nv_monto_por_punto integer:=0;
begin
	--Verificamos la existencia de la fecha de asignacion de bolsa puntos en el rango de vencimientos
	select count(*)
	into nv_cont_reg_venc
	from vencimientos 
	where vd_fecha_asig_bp between fecha_ini_validez and fecha_fin_validez;

	if nv_cont_reg_venc>0 then
		select fecha_fin_validez
		into vd_fecha_caducidad
		from vencimientos 
		where vd_fecha_asig_bp between fecha_ini_validez and fecha_fin_validez;
	else 
		vd_fecha_caducidad := to_char(CURRENT_DATE,'YYYY-MM-DD');--la fecha del dia
	end if;
	

	-------------------
	--puntaje asignado
	-------------------
	--Se verifica existencia de regla para el monto de compra
	select count(*)
	into nv_cont_regla
	from reglas
	where nv_monto_compra between limite_inferior and limite_superior;

	if nv_cont_regla<>0 then 
		select monto_por_punto 
		into nv_monto_por_punto
		from reglas
		where nv_monto_compra between limite_inferior and limite_superior;
		--Para obtener el puntaje adquirido según el monto de compra
		nv_puntaje_asignado:= round(nv_monto_compra/nv_monto_por_punto);
	end if;

	
	UPDATE public.bolsa_puntos 
	SET fecha_caducidad_puntaje= vd_fecha_caducidad,
		puntaje_asignado = nv_puntaje_asignado
	where id=new.id;

	
	return new;
end;
$$
Language plpgsql;
/

create trigger or replace TR_POBLAR_BOLSA_PUNTOS1 after insert on "bolsa_puntos"
for each row
execute procedure F_TR_POBLAR_BOLSA_PUNTOS1();
/

	