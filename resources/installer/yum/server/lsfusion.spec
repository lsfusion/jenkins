%define __jar_repack %{nil}
%define lsfusion_major_version <lsfusion-major-version>
%define lsfusion_version <lsfusion-version>
%define lsfusion_release <lsfusion-release>
%define lsfusion_title <lsfusion-title>
%define lsfusion_name lsfusion%{lsfusion_major_version}-server
%define lsfusion_home /usr/share/%{lsfusion_name}
%define lsfusion_group lsfusion
%define lsfusion_user lsfusion

Summary:    %{lsfusion_title}
Name:       %{lsfusion_name}
Version:    %{lsfusion_version}
BuildArch:  noarch
Release:    %{lsfusion_release}
License:    GNU Lesser General Public License v3.0
Group:      Networking/Daemons
URL:        https://lsfusion.org
Source0:    server.jar
Source1:    lsfusion.conf
Source2:    settings.properties
Source3:    %{lsfusion_name}.service
Conflicts:  tomcat, tomcat7, tomcat8
BuildRoot:  %{_tmppath}/lsfusion%{lsfusion_major_version}-%{version}-%{release}-root-%(%{__id_u} -n)

%description
%{lsfusion_title}

%prep

%build

%install
install -d -m 755 %{buildroot}/%{lsfusion_home}/
install    -m 644 %{SOURCE0} %{buildroot}/%{lsfusion_home}

# Put logging in /var/log and link back.
install -d -m 755 %{buildroot}/var/log/%{lsfusion_name}/
cd %{buildroot}/%{lsfusion_home}/
ln -s /var/log/%{lsfusion_name}/ logs
cd -

# Put conf in /etc/ and link back.
install -d -m 755 %{buildroot}/%{_sysconfdir}/%{lsfusion_name}
install    -m 644 %{SOURCE2} %{buildroot}/%{_sysconfdir}/%{lsfusion_name}
install    -m 644 %{SOURCE1} %{buildroot}/%{_sysconfdir}/%{lsfusion_name}
cd %{buildroot}/%{lsfusion_home}/
ln -s %{_sysconfdir}/%{lsfusion_name} conf
cd -

# Drop init script
install -d -m 755 %{buildroot}/%{_unitdir}
install    -m 644 %{SOURCE3} %{buildroot}/%{_unitdir}

# Create lib directory
install -d -m 755 %{buildroot}/%{_sharedstatedir}/lsfusion

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
%{lsfusion_home}
%{_sharedstatedir}/lsfusion
%config(noreplace) %{_sysconfdir}/%{lsfusion_name}
%defattr(-,%{lsfusion_user},%{lsfusion_group})
%{_rundir}/%{lsfusion_name}
/var/log/%{lsfusion_name}

%post

%preun

%postun

%changelog