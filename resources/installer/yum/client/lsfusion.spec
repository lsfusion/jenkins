%define __jar_repack %{nil}
%define lsfusion_major_version <lsfusion-major-version>
%define lsfusion_version <lsfusion-version>
%define lsfusion_title <lsfusion-title>
%define lsfusion_name lsfusion%{lsfusion_major_version}-client
%define lsfusion_home /usr/share/%{lsfusion_name}
%define lsfusion_group lsfusion
%define lsfusion_user lsfusion
%define lsfusion_cache_home /var/cache/%{lsfusion_name}

Summary:    %{lsfusion_title}
Name:       %{lsfusion_name}
Version:    %{lsfusion_version}
BuildArch:  noarch
Release:    1
License:    GNU Lesser General Public License v3.0
Group:      Networking/Daemons
URL:        https://lsfusion.org
Source0:    apache-tomcat-9.0.20.tar.gz
Source1:    client.war
Source2:    lsfusion.conf
Source3:    %{lsfusion_name}.service
Source4:    ROOT.xml
Source5:    lsfusion.logrotate
Conflicts:  tomcat, tomcat7, tomcat8
BuildRoot:  %{_tmppath}/lsfusion%{lsfusion_major_version}-%{version}-%{release}-root-%(%{__id_u} -n)

%description
%{lsfusion_title}

%prep
unzip %{SOURCE1} -d client
%setup -q -n apache-tomcat-9.0.20

%build

%install
install -d -m 755 %{buildroot}/%{lsfusion_home}/
cp -R * %{buildroot}/%{lsfusion_home}/

# Remove all webapps. Put webapps in /var/lib and link back.
rm -rf %{buildroot}/%{lsfusion_home}/webapps

# Put lsFusion
cd ../client
install -d -m 775 %{buildroot}/%{lsfusion_home}/client
cp -R * %{buildroot}/%{lsfusion_home}/client
cd -

# Remove *.bat
rm -f %{buildroot}/%{lsfusion_home}/bin/*.bat

# Put logging in /var/log and link back.
rm -rf %{buildroot}/%{lsfusion_home}/logs
install -d -m 755 %{buildroot}/var/log/%{lsfusion_name}/
cd %{buildroot}/%{lsfusion_home}/
ln -s /var/log/%{lsfusion_name}/ logs
cd -

# Put conf in /etc/ and link back.
install -d -m 755 %{buildroot}/%{_sysconfdir}
mv %{buildroot}/%{lsfusion_home}/conf %{buildroot}/%{_sysconfdir}/%{lsfusion_name}
cd %{buildroot}/%{lsfusion_home}/
ln -s %{_sysconfdir}/%{lsfusion_name} conf
cd -

# Put temp and work to /var/cache and link back.
install -d -m 775 %{buildroot}%{lsfusion_cache_home}
mv %{buildroot}/%{lsfusion_home}/temp %{buildroot}/%{lsfusion_cache_home}/
mv %{buildroot}/%{lsfusion_home}/work %{buildroot}/%{lsfusion_cache_home}/
cd %{buildroot}/%{lsfusion_home}/
ln -s %{lsfusion_cache_home}/temp
ln -s %{lsfusion_cache_home}/work
chmod 775 %{buildroot}/%{lsfusion_cache_home}/temp
chmod 775 %{buildroot}/%{lsfusion_cache_home}/work
cd -

# Drop init script
install -d -m 755 %{buildroot}/%{_unitdir}
install    -m 644 %{SOURCE3} %{buildroot}/%{_unitdir}

# Drop sysconfig script
install -d -m 755 %{buildroot}/%{_sysconfdir}
install    -m 644 %{SOURCE2} %{buildroot}/%{_sysconfdir}/%{lsfusion_name}

# Drop context xml
install -d -m 755 %{buildroot}/%{_sysconfdir}/%{lsfusion_name}/Catalina/localhost
install    -m 644 %{SOURCE4} %{buildroot}/%{_sysconfdir}/%{lsfusion_name}/Catalina/localhost

# Drop logrotate script
install -d -m 755 %{buildroot}/%{_sysconfdir}/logrotate.d
install    -m 644 %{SOURCE5} %{buildroot}/%{_sysconfdir}/logrotate.d/%{lsfusion_name}

# Create run directory
install -d -m 755 %{buildroot}/%{_rundir}/%{lsfusion_name}

%clean
rm -rf %{buildroot}

%pre
getent group %{lsfusion_group} >/dev/null || groupadd %{lsfusion_group}
getent passwd %{lsfusion_user} >/dev/null || useradd --comment "lsFusion Daemon User" -M -s /bin/nologin -g %{lsfusion_group} %{lsfusion_user}

%files
%defattr(-,root,root)
%{_unitdir}/%{lsfusion_name}.service
%{_sysconfdir}/logrotate.d/%{lsfusion_name}
%config(noreplace) %{_sysconfdir}/%{lsfusion_name}
%defattr(-,%{lsfusion_user},%{lsfusion_group})
%{lsfusion_home}
%{lsfusion_cache_home}
%{_rundir}/%{lsfusion_name}
/var/log/%{lsfusion_name}

%post

%preun

%postun

%changelog
