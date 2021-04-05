/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {
  title: 'lsfusion documentation',
  tagline: 'lsfusion documentation',
  url: 'https://danchanka.github.io',
  baseUrl: '/platform/',
  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',
  organizationName: 'danchanka', // Usually your GitHub org/user name.
  projectName: 'platform', // Usually your repo name.
  themeConfig: {
    navbar: {
      title: 'lsfusion',
      logo: {
        alt: 'My Site Logo',
        src: 'img/logo.svg',
      },
      items: [
        {
          to: 'docs/Learn',
          activeBasePath: 'docs',
          label: 'Docs',
          position: 'left',
        },
        {to: 'blog', label: 'Blog', position: 'left'},
        {
          href: 'https://github.com/danchanka/platform',
          label: 'GitHub',
          position: 'right',
        },
        {
          type: 'docsVersionDropdown',
          position: 'right',
          // // Add additional dropdown items at the beginning/end of the dropdown.
          // dropdownItemsBefore: [],
          // dropdownItemsAfter: [{to: '/versions', label: 'All versions'}],
          // // Do not add the link active class when browsing docs.
          // dropdownActiveClassDisabled: true,
          // docsPluginId: 'default',
        },
        {
          type: 'localeDropdown',
          position: 'right',
        },        
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'lsf docs',
              to: 'docs/Learn',
            },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Stack Overflow',
              href: 'https://ru.stackoverflow.com/questions/tagged/lsfusion',
            },
            {
              label: 'Slack',
              href: 'https://slack.lsfusion.org',
            },
            {
              label: 'Twitter',
              href: 'https://twitter.com/lsfusion',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'Blog',
              to: 'blog',
            },
            {
              label: 'Habr',
              href: 'https://habr.com/ru/company/lsfusion/blog/',
            },
            {
              label: 'GitHub',
              href: 'https://github.com/lsfusion/platform',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} lsFusion Foundation. Built with Docusaurus.`,
    },
    // prism: {
    //   theme: require('prism-react-renderer/themes/okaidia'),
    // },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          editUrl:
            'https://github.com/danchanka/platform/edit/master/',
          editLocalizedFiles: true
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          editUrl:
            'https://github.com/danchanka/platform/edit/master/blog/',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'ru'],
    localeConfigs: {
      en: {
        label: 'English',
      },
      ru: {
        label: 'Русский',
      },
    },
  },  
};
